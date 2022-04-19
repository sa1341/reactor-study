package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.Member
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: R2dbcRepository<Member, Long> {
}
