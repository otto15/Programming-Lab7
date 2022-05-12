package com.otto15.server.response;

import com.otto15.common.network.Response;
import com.otto15.common.network.Serializer;
import com.otto15.server.channels.ChannelState;
import com.otto15.server.logging.LogConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class ResponseSender {

    private final SocketChannel channel;
    private final Map<SocketChannel, ByteBuffer> channels;
    private final Selector selector;
    private final Map<SocketChannel, ChannelState> channelState;

    public ResponseSender(SocketChannel channel, Map<SocketChannel, ByteBuffer> channels, Selector selector, Map<SocketChannel, ChannelState> channelState) {
        this.channel = channel;
        this.channels = channels;
        this.selector = selector;
        this.channelState = channelState;
    }

    public void send(Response response) {
        Serializer serializer = new Serializer();

        try {
            ByteBuffer buffer = ByteBuffer.wrap(serializer.serialize(response));
            int responseLen = 0;
            while (buffer.hasRemaining()) {
                int bytesWritten = channel.write(buffer);
                responseLen += bytesWritten;
            }
            channels.put(channel, ByteBuffer.allocate(0));
            channelState.put(channel, ChannelState.READY_TO_READ);
            channel.register(selector, SelectionKey.OP_READ);
            LogConfig.LOGGER.info("Sent response {} bytes to {}", responseLen, channel.getRemoteAddress());
        } catch (IOException e) {
            channels.put(channel, null);
        }
    }

}
