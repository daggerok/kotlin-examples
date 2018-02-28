package daggerok

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.nio.NioEventLoopGroup

@Suppress("UNUSED_PARAMETER")
class NettyServer : ChannelInboundHandlerAdapter() {

  companion object {
    fun start(port: Int = 8080) {
      val boss = NioEventLoopGroup()
      val worker = NioEventLoopGroup()
      val serverBootstrap = ServerBootstrap()
      serverBootstrap.group(boss, worker)
    }
  }
}
