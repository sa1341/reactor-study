package com.reactive.reactorstudy.reactor.common // ktlint-disable filename

data class EmptyImage(
    override val id: String = "",
    override val name: String = "",
    override val url: String = ""
) : Image(id, name, url)
