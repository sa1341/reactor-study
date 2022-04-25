package com.reactive.reactorstudy.member.api

import com.reactive.reactorstudy.member.entity.Account
import com.reactive.reactorstudy.member.entity.AccountCustomResult
import com.reactive.reactorstudy.member.entity.Customer
import com.reactive.reactorstudy.member.repository.AccountMongoRepository
import com.reactive.reactorstudy.member.repository.CustomerRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.query.Criteria
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
    private val customerRepository: CustomerRepository,
    private val mongoTemplate: MongoTemplate
) {

    @PostMapping("/v1/account")
    fun setMember(): ResponseEntity<String> {

        val customer = Customer("sa1341", "jeancalm")
        customerRepository.save(customer)

        var account1 = Account(UUID.randomUUID().toString(), "종합계좌", customer._id)
        var account2 = Account(UUID.randomUUID().toString(), "근거계좌", customer._id)

        accountMongoRepository.save(account1)
        accountMongoRepository.save(account2)

        return ResponseEntity.ok("success")
    }

    @GetMapping("/v1/account")
    fun getTradeList(): ResponseEntity<AccountCustomResult> {

        val lookup = LookupOperation.newLookup()
            .from("account")
            .localField("_id")
            .foreignField("customerId")
            .`as`("accounts")

        val newAggregation = Aggregation.newAggregation(lookup)

        val result = mongoTemplate.aggregate(newAggregation, "customer", AccountCustomResult::class.java).uniqueMappedResult

        return ResponseEntity.ok(result)
    }
}
