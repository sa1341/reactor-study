package com.reactive.reactorstudy.member.entity

import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

@RedisHash("team")
data class Team(
    @Id
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    var age: Int? = 0
)
