package com.reactive.reactorstudy.netty.handler

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class NettyEchoServerHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is String) {
            log.info { "Received: $msg" }
            ctx.writeAndFlush(msg)
                .addListener(ChannelFutureListener.CLOSE)
        }
    }
}
