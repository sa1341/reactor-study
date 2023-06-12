package com.reactive.reactorstudy.netty // ktlint-disable filename

import com.reactive.reactorstudy.netty.handler.NettyEchoClientHandler
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LoggingHandler

fun main() {
    val workGroup = NioEventLoopGroup(1)

    try {
        val bootStrap = Bootstrap()
        val client = bootStrap
            .group(workGroup)
            .channel(NioSocketChannel::class.java)
            .handler(
                object : ChannelInitializer<Channel>() {
                    override fun initChannel(ch: Channel) {
                        ch.pipeline().addLast(
                            LoggingHandler(),
                            StringEncoder(),
                            StringDecoder(),
                            NettyEchoClientHandler()
                        )
                    }
                }
            )

        client.connect("localhost", 8080)
            .channel().closeFuture().sync()
    } finally {
        workGroup.shutdownGracefully()
    }
}
