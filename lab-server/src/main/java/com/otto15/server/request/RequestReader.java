package com.otto15.server.request;

import com.otto15.common.network.Request;
import com.otto15.common.network.Serializer;
import com.otto15.server.channels.ChannelState;
import com.otto15.server.logging.LogConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class RequestReader implements Runnable {

    private static final int STANDARD_BUFFER_SIZE = 1024;
    private final SocketChannel channel;
    private final Map<SocketChannel, ByteBuffer> channels;
    private final Selector selector;
    private final Map<SocketChannel, ChannelState> channelState;

    public RequestReader(SocketChannel channel, Map<SocketChannel, ByteBuffer> channels, Selector selector, Map<SocketChannel, ChannelState> channelState) {
        this.channel = channel;
        this.channels = channels;
        this.selector = selector;
        this.channelState = channelState;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(STANDARD_BUFFER_SIZE);
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                synchronized (LogConfig.LOGGER) {
                    LogConfig.LOGGER.info("Connection closed {}", channel.getRemoteAddress());
                }
                channels.remove(channel);
                channel.close();
                return;
            }
            ByteBuffer newBuffer = ByteBuffer.allocate(channels.get(channel).capacity() + bytesRead);
            newBuffer.put(channels.get(channel).array());
            newBuffer.put(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
            channels.put(channel, newBuffer);

            Serializer serializer = new Serializer();
            Request request = (Request) serializer.deserialize(channels.get(channel).array());

            if (request != null) {
                LogConfig.LOGGER.info("Got from {}", channel.getRemoteAddress());
                channelState.put(channel, ChannelState.READY_TO_WRITE);
                channel.register(selector, SelectionKey.OP_WRITE);
            } else {
                LogConfig.LOGGER.warn("There is still data to read");
                channelState.put(channel, ChannelState.READY_TO_READ);
            }
        } catch (IOException e) {
            channels.put(channel, null);
        }
    }

}
