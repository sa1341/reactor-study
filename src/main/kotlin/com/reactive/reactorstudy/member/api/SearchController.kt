package com.reactive.reactorstudy.member.api

import com.reactive.reactorstudy.member.entity.Account
import com.reactive.reactorstudy.member.entity.Customer
import com.reactive.reactorstudy.member.repository.AccountMongoRepository
import com.reactive.reactorstudy.member.repository.CustomerRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping(value = ["/api"])
@RestController
class SearchController(
    private val accountMongoRepository: AccountMongoRepository,
    private val customerRepository: CustomerRepository
) {

    @PostMapping("/v1/account")
    fun setMember(): ResponseEntity<String> {

        val customer = Customer()
        customer.id = "sa1341"

        var account1 = Account(UUID.randomUUID().toString(),"sa1341","02000165183", "junyoung")
        var account2 = Account(UUID.randomUUID().toString(),"sa1341","02000140147", "jeancalm")

        accountMongoRepository.save(account1)
        accountMongoRepository.save(account2)

        var accounts = mutableListOf<Account>()
        accounts.add(account1)
        accounts.add(account2)

        customer.accounts = accounts

        customerRepository.save(customer)

        return ResponseEntity.ok("success")
    }

    @GetMapping("/v1/account")
    fun getTradeList(): ResponseEntity<List<Account>> {

        val optionalCustomer = customerRepository.findById("sa1341")

        if (optionalCustomer.isPresent) {
            val customer = optionalCustomer.get()
            return ResponseEntity.ok(customer.accounts)
        }

        return ResponseEntity.ok(null)
    }
}
