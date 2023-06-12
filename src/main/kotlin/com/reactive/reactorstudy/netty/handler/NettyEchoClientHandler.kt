package com.reactive.reactorstudy.netty.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class NettyEchoClientHandler : ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush("This is client")
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is String) {
            log.info { "Received: $msg" }
        }
    }
}
