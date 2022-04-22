package com.reactive.reactorstudy.member.api

import com.reactive.reactorstudy.member.entity.Member
import com.reactive.reactorstudy.member.entity.Trade
import com.reactive.reactorstudy.member.entity.User
import com.reactive.reactorstudy.member.repository.UserRedisRepository
import com.reactive.reactorstudy.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@RequestMapping(value = ["/api"])
@RestController
class MemberApi(
    private val memberService: MemberService,
    private val userRedisRepository: UserRedisRepository
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

    @PostMapping("/v1/redis")
    fun setMember(): ResponseEntity<String> {

        var tradeList = mutableListOf<Trade>()
        tradeList.add(Trade("20220420", 1, BigDecimal(30000)))
        tradeList.add(Trade("20220421", 2, BigDecimal(10000)))
        tradeList.add(Trade("20220422", 3, BigDecimal(20000)))

        val user = User("02000140147", "jeancalm"
            , "a79007714@gmail.com", 30, tradeList)

        userRedisRepository.save(user)

        return ResponseEntity.ok("success")
    }

    @GetMapping("/v1/users")
    fun getTradeList(): ResponseEntity<List<Trade>> {

        val optionalUser = userRedisRepository.findById("02000140147")

        if (optionalUser.isPresent) {
            val user = optionalUser.get()
            return ResponseEntity.ok(user.trades)
        }

        return ResponseEntity.ok(null)
    }
}
