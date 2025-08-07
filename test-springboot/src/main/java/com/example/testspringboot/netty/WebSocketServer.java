package com.example.testspringboot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.io.FileInputStream;

public class WebSocketServer {

    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            pipeline.addLast(new WebSocketFrameHandler());
                        }
                    });

            Channel ch = b.bind(port).sync().channel();
            System.out.println("WebSocket server started at port " + port);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new WebSocketServer(8082).start();
    }
}

class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            // 处理文本帧
            String text = ((TextWebSocketFrame) frame).text();
            System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的文本消息：" + text);
        } else if (frame instanceof BinaryWebSocketFrame) {
            // 处理二进制帧
            ByteBuf byteBuf = frame.content();
            System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的二进制消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        } else if (frame instanceof CloseWebSocketFrame) {
            // 处理关闭帧
            System.out.println("收到关闭帧");
            ctx.channel().close();
        } else if (frame instanceof PingWebSocketFrame) {
            // 处理Ping帧
            System.out.println("收到Ping帧");
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
        } else if (frame instanceof PongWebSocketFrame) {
            // 处理Pong帧
            System.out.println("收到Pong帧");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
