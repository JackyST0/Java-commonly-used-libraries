package com.example.testspringboot.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;

import java.net.URI;

public class WebSocketClient {

    private final String uri;

    public WebSocketClient(String uri) {
        this.uri = uri;
    }

    public void start() throws Exception {
        URI uri = new URI(this.uri);
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();

        if (port == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            int finalPort = port;
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            if (sslCtx != null) {
                                pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, finalPort));
                            }
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new ChunkedWriteHandler());
                            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, null);
                            pipeline.addLast(new WebSocketClientProtocolHandler(handshaker));
                            pipeline.addLast(new WebSocketClientHandler());
                        }
                    });

            Channel ch = b.connect(uri.getHost(), port).sync().channel();
            ch.writeAndFlush(new TextWebSocketFrame("Hello, Netty WebSocket!"));
            ch.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new WebSocketClient("ws://127.0.0.1:8082/ws").start();
    }
}

class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 连接建立后，发送初始消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("Hello, server!"));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            System.out.println("Received message: " + textFrame.text());
        } else {
            System.out.println("Received unsupported frame: " + msg.getClass().getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
