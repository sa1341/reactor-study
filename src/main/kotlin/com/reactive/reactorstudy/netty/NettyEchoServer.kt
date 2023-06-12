package com.reactive.reactorstudy.netty // ktlint-disable filename

import com.reactive.reactorstudy.netty.handler.NettyEchoServerHandler
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.DefaultEventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.FutureListener
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun main() {
    val parentGroup = NioEventLoopGroup()
    val childGroup = NioEventLoopGroup(4)
    val eventExecutorGroup = DefaultEventLoopGroup(4)

    try {
        val serverBootStrap = ServerBootstrap()
        val server = serverBootStrap
            .group(parentGroup, childGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.pipeline().addLast(
                        eventExecutorGroup,
                        LoggingHandler(LogLevel.INFO)
                    )
                    ch.pipeline().addLast(
                        StringEncoder(),
                        StringDecoder(),
                        NettyEchoServerHandler()
                    )
                }
            })

        server.bind(8080).sync()
            .addListener(object : FutureListener<Void> {
                override fun operationComplete(future: Future<Void>) {
                    if (future.isSuccess) {
                        log.info { "Success to bind 8080" }
                    } else {
                        log.error { "Fail to bind 8080" }
                    }
                }
            })
            .channel().closeFuture().sync()
    } finally {
        parentGroup.shutdownGracefully()
        childGroup.shutdownGracefully()
        eventExecutorGroup.shutdownGracefully()
    }
}
