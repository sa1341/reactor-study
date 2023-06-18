package com.reactive.reactorstudy.reactor.service

import com.reactive.reactorstudy.reactor.common.EmptyImage
import com.reactive.reactorstudy.reactor.common.Image
import com.reactive.reactorstudy.reactor.common.User
import com.reactive.reactorstudy.reactor.entity.UserEntity
import com.reactive.reactorstudy.reactor.repository.UserReactorRepository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

const val url = "http://localhost:8081"

class UserReactorService(
    private val userReactorRepository: UserReactorRepository
) {

    private val webClient = WebClient.create(url)

    fun getUserById(userId: String): Mono<User> {
        return userReactorRepository.findById(userId)
            .flatMap {
                val userEntity: UserEntity = it
                val imageId = it.profileImageId
                val uriVariableMap: Map<String, String> = mapOf("imageId" to imageId)
                webClient.get()
                    .uri("/api/images/{imageId}", uriVariableMap)
                    .retrieve()
                    .toEntity(ImageResponse::class.java)
                    .map { it.body }
                    .map {
                        Image(
                            id = it!!.id,
                            name = it!!.name,
                            url = it!!.url
                        )
                    }.switchIfEmpty(Mono.just(EmptyImage()))
                    .map {
                        User(
                            id = userEntity.id,
                            name = userEntity.name,
                            age = userEntity.age,
                            profileImage = emptyList(),
                            articles = emptyList(),
                            followCount = 0
                        )
                    }
            }
    }
}
