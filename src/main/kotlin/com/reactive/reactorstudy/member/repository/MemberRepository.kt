package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.entity.Member
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: R2dbcRepository<Member, Long> {
}
