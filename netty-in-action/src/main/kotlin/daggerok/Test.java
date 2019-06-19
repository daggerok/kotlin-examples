package daggerok;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

@Sharable
class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    System.out.println(
        "Server received: " + in.toString(CharsetUtil.UTF_8));
    ctx.write(in);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
       .addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx,
                              Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}

class EchoServer {

  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
    }
    int port = Integer.parseInt(args[0]);
    new EchoServer(port).start();
  }

  public void start() throws Exception {

    final EchoServerHandler serverHandler = new EchoServerHandler();
    final EventLoopGroup group = new NioEventLoopGroup();

    try {

      final ServerBootstrap serverBootstrap = new ServerBootstrap()
          .group(group)
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(port))
          .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         public void initChannel(SocketChannel ch)
             throws Exception {
           ch.pipeline().addLast(serverHandler);
         }
       });

      final ChannelFuture channelFuture = serverBootstrap.bind().sync();

      channelFuture.channel().closeFuture().sync();

    } finally {
      group.shutdownGracefully().sync();
    }
  }
}
