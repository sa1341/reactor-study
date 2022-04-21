package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.entity.Team
import org.springframework.data.repository.CrudRepository

interface TeamRedisRepository: CrudRepository<Team, String> {
}
