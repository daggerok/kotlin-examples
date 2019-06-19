package daggerok

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.CharsetUtil
import org.slf4j.LoggerFactory

@Sharable // Can be shared among channels
internal class NettyEchoServerHandler : ChannelInboundHandlerAdapter() {

  companion object {
    private val log = LoggerFactory.getLogger(NettyEchoServerHandler::class.java)
  }

  override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
    val request = msg as ByteBuf
    val requestPayload = request.toString(CharsetUtil.UTF_8)
    log.info("server handling client input: {}", requestPayload)

    val responsePayload = requestPayload.toUpperCase()
    val bytes = responsePayload.toByteArray(CharsetUtil.UTF_8)
    val response = Unpooled.wrappedBuffer(bytes)
    ctx.write(response)
  }

  override fun channelReadComplete(ctx: ChannelHandlerContext) {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
        .addListener(ChannelFutureListener.CLOSE)
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    cause.printStackTrace()
    ctx.close()
  }
}

class NettyEchoServer(private val port: Int = 8080) {

  companion object {
    val log = LoggerFactory.getLogger(NettyEchoServerHandler::class.java)
  }

  @Throws(Exception::class)
  fun start() {

    val group = NioEventLoopGroup()

    try {

      val serverBootstrap = ServerBootstrap()
          .group(group)
          .channel(NioServerSocketChannel::class.java)
          .localAddress(port)
          .childHandler(object : ChannelInitializer<SocketChannel>() {

            @Throws(Exception::class)
            public override fun initChannel(ch: SocketChannel) {
              ch.pipeline().addLast(NettyEchoServerHandler())
            }
          })

      val channelFuture = serverBootstrap.bind().sync()
      channelFuture.channel().closeFuture().sync()
    }

    finally {
      group.shutdownGracefully().sync()
    }
  }
}
