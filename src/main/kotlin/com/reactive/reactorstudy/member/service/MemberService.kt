package com.reactive.reactorstudy.member.service

import com.reactive.reactorstudy.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/*@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun saveAllMembers(members: Flux<Member>): Flux<Member> {
        return memberRepository.saveAll(members)
    }

    fun saveMember(member: Member): Mono<Member> {
        return memberRepository.save(member)
    }
}*/
