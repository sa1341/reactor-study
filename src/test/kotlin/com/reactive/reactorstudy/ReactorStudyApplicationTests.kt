package com.reactive.reactorstudy

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ReactorStudyApplicationTests {

	@Test
	fun contextLoads() {
		val name = "jeancalm"
		println("Welcome to $name")
	}

}
