package com.reactive.reactorstudy.member.api

import com.reactive.reactorstudy.member.Member
import com.reactive.reactorstudy.member.service.MemberService
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RequestMapping(value = ["/api"])
@RestController
class MemberApi(
    private val memberService: MemberService
) {

    @GetMapping(value = ["/v1/members"])
    fun recentMembers(): MutableList<Member> {

        val members = getMembers()

        val stopWatch = StopWatch()

        stopWatch.start()

        members.forEach {
            println("id: ${it.id}, name: ${it.name}")
        }

        stopWatch.stop()

        println(stopWatch.totalTimeMillis)

        return members
    }

    @GetMapping(value = ["/v2/members"])
    fun recentReactiveMembers(): Flux<Member> {

        val members = getMembers()
        val stopWatch = StopWatch()

        stopWatch.start()

        Flux.fromIterable(members)
            .take(100)
            .subscribe {
                println("id: ${it.id}, name: ${it.name}")
            }

        stopWatch.stop()

        println(stopWatch.totalTimeMillis)

        return Flux.fromIterable(members)
    }

    @PostMapping("/v1/members")
    fun saveMembers(): Flux<Member> {
        val members = getMembers()
        return memberService.saveAllMembers(Flux.fromIterable(members))
    }

    @PostMapping("/v1/member")
    fun saveMember(): Mono<Member> {
        val member = Member( "jeancalm", 30)
        return memberService.saveMember(member)
    }

    private fun getMembers(): MutableList<Member> {

        val endIdx = 1000L
        val members = mutableListOf<Member>()

        for (i in 1L..endIdx) {
            members.add(Member("name_$i", 10))
        }
        return members
    }
}
