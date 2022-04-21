package com.reactive.reactorstudy.redis

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
class RedisTemplateTests {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<*, *>

    @Test
    fun testString() {
        val operations = redisTemplate.opsForValue()
        val result = operations["dog"]
        println("result: $result")
    }
}
