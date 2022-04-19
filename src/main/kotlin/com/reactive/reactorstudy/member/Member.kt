package com.reactive.reactorstudy.member

import com.reactive.reactorstudy.global.common.EntityAuditing
import org.springframework.data.relational.core.mapping.Table

@Table("members")
class Member(
    var name: String,
    var age: Int
): EntityAuditing()
