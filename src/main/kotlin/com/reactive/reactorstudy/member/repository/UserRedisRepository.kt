package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRedisRepository: CrudRepository<User, String> {
}
