package com.reactive.reactorstudy.reactor.common

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val profileImage: List<Image>,
    val articles: List<Article>,
    val followCount: Long
)
