package daggerok

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.CharsetUtil
import org.slf4j.LoggerFactory

@Sharable // Can be shared among channels
internal class NettyEchoClientHandler(private val payload: String) : SimpleChannelInboundHandler<ByteBuf>() {

  companion object {
    private val log = LoggerFactory.getLogger(NettyEchoClientHandler::class.java)
  }

  override fun channelActive(ctx: ChannelHandlerContext?) {
    log.debug("connection established.")
    ctx?.writeAndFlush(Unpooled.copiedBuffer(payload, CharsetUtil.UTF_8))
  }

  override fun channelRead0(ctx: ChannelHandlerContext?, msg: ByteBuf?) {
    val response = msg?.toString(CharsetUtil.UTF_8)
    log.info("client received server output: {}", response)
  }

  override fun channelReadComplete(ctx: ChannelHandlerContext) {
    log.debug("on complete")
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
        .addListener(ChannelFutureListener.CLOSE)
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
    log.error("on error: {}", cause?.localizedMessage)
    ctx?.close()
  }
}

class NettyEchoClient(private val host: String = "127.0.0.1", private val port: Int = 8080) {

  companion object {
    val log = LoggerFactory.getLogger(NettyEchoClientHandler::class.java)
  }

  fun send(request: String): NettyEchoClient {

    val group = NioEventLoopGroup()

    try {

      val bootstrap = Bootstrap()
          .group(group)
          .channel(NioSocketChannel::class.java)
          .remoteAddress(host, port)
          .handler(object : ChannelInitializer<SocketChannel>() {

            @Throws(Exception::class)
            public override fun initChannel(ch: SocketChannel) {
              ch.pipeline()
                  .addLast(NettyEchoClientHandler(request))
            }
          })

      val channelFuture = bootstrap.connect().sync()

      channelFuture.channel().closeFuture().sync()
    }

    finally {
      group.shutdownGracefully()
    }

    return this
  }
}
