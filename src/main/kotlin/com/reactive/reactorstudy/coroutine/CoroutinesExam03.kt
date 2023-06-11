package com.reactive.reactorstudy.coroutine

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports

data class UserInfo(
    val name: String,
    val lastName: String,
    val id: Int
)

lateinit var user: UserInfo

fun main() = runBlocking {
    asycnGetUserInfo(1)

    delay(1000)

    println("User ${user.id} is ${user.name}")
}

fun asycnGetUserInfo(id: Int) = GlobalScope.async {
    delay(1100)
    user = UserInfo(name = "junyoung", id = id, lastName = "jeancalm")
}
