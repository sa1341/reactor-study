package com.reactive.reactorstudy.member.api

import com.reactive.reactorstudy.member.entity.Trade
import com.reactive.reactorstudy.member.entity.User
import com.reactive.reactorstudy.member.repository.UserRedisRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal


@RequestMapping(value = ["/api"])
@RestController
class CashController(
    private val userRedisRepository: UserRedisRepository
) {

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
