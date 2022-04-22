package com.reactive.reactorstudy.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("user")
class User (

    @Id
    val acno: String,
    var name: String,
    var email: String,
    var age: Int,
    var trades: MutableList<Trade> = mutableListOf()
)
