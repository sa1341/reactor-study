package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.entity.Account
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountMongoRepository: MongoRepository<Account, String> {
}
