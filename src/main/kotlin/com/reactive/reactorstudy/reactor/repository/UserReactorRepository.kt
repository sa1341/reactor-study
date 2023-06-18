package com.reactive.reactorstudy.reactor.repository

import com.reactive.reactorstudy.reactor.entity.UserEntity
import mu.KotlinLogging
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

class UserReactorRepository {

    private val userMap: Map<String, UserEntity> = initUserMap()

    fun findById(userId: String): Mono<UserEntity> {
        return Mono.create {
            log.info { "UserRepository.findById: $userId" }

            kotlin.runCatching {
                TimeUnit.SECONDS.sleep(1)
            }.getOrElse { throw RuntimeException("Exception") }

            val user = userMap[userId]

            if (user == null) {
                it.success()
            } else {
                it.success(user)
            }
        }
    }

    private fun initUserMap(): Map<String, UserEntity> {
        val user = UserEntity(
            id = "1234",
            name = "junyoung",
            age = 32,
            profileImageId = "1",
            password = "wnsdud1234"
        )

        return mapOf("1234" to user)
    }
}
