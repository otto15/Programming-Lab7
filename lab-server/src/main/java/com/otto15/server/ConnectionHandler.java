package com.otto15.server;


import com.otto15.common.network.Request;
import com.otto15.server.channels.ChannelState;
import com.otto15.server.response.ResponseSender;
import com.otto15.server.logging.LogConfig;
import com.otto15.server.request.RequestExecutor;
import com.otto15.common.network.Serializer;
import com.otto15.common.state.PerformanceState;
import com.otto15.server.request.RequestReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;


public final class ConnectionHandler implements Runnable {

    private static final int SELECT_DELAY = 1000;
    private final Map<SocketChannel, ByteBuffer> channels = Collections.synchronizedMap(new HashMap<>());
    private final ForkJoinPool forkJoinPool = new ForkJoinPool((int) (Runtime.getRuntime().availableProcessors() * 0.5 * (1 + 10)));
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final RequestExecutor requestExecutor;
    private final Map<SocketChannel, ChannelState> channelsState = Collections.synchronizedMap(new HashMap<>());
    private final PerformanceState performanceState;

    public ConnectionHandler(RequestExecutor requestExecutor, PerformanceState performanceState) throws IOException {
        this.performanceState = performanceState;
        this.requestExecutor = requestExecutor;
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        while (true) {
            try {
                serverChannel.socket().bind(new InetSocketAddress(inputPort()));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Port must be in the range from 0 to 65535.");
            } catch (IOException e) {
                System.out.println("Port is already in use.");
            }
        }
    }

    private int inputPort() throws IOException {
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter port:");
                String port = reader.readLine();
                if (port == null) {
                    throw new IOException();
                }
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                System.out.println("Enter number");
            }
        }
    }

    public void run() {
        try {
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
        LogConfig.LOGGER.info("Server closed");
    }

    private void close() {
        try {
            selector.close();
            serverChannel.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listen() throws IOException {
        while (performanceState.getPerformanceStatus()) {
            selector.select(SELECT_DELAY);
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            read(key);
                        } else if (key.isWritable()) {
                            write(key);
                        }
                    }
                } catch (IOException e) {
                    if (key.channel().isOpen()) {
                        kill((SocketChannel) key.channel());
                    }
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel channel = serverChannel.accept();
        channels.put(channel, ByteBuffer.allocate(0));
        SocketAddress address = channel.getRemoteAddress();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channelsState.put(channel, ChannelState.READY_TO_READ);
        LogConfig.LOGGER.info("New connection accepted {}", address);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channelsState.get(channel) == ChannelState.READY_TO_READ) {
            channelsState.put(channel, ChannelState.READING);
            new Thread(new RequestReader(channel, channels, selector, channelsState))
                    .start();
        }
        if (channels.get((SocketChannel) key.channel()) == null) {
            throw new IOException();
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        if (channelsState.get(channel) == ChannelState.READY_TO_WRITE) {
            channelsState.put(channel, ChannelState.WRITING);
            Serializer serializer = new Serializer();

            Request request = (Request) serializer.deserialize(channels.get((SocketChannel) key.channel()).array());
            ResponseSender responseSender = new ResponseSender(channel, channels, selector, channelsState);
            CompletableFuture.supplyAsync(() -> requestExecutor.execute(request), forkJoinPool)
                    .thenAcceptAsync(responseSender::send, forkJoinPool);
        }
        if (channels.get((SocketChannel) key.channel()) == null) {
            throw new IOException();
        }
    }

    private void kill(SocketChannel channel) throws IOException {
        SocketAddress address = channel.getRemoteAddress();
        channels.remove(channel);
        channel.close();
        LogConfig.LOGGER.info("Connection closed {}", address);
    }
}
