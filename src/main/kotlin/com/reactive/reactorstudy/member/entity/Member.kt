package com.reactive.reactorstudy.member.entity

import com.reactive.reactorstudy.global.common.EntityAuditing

class Member(
    var name: String,
    var age: Int
): EntityAuditing()
