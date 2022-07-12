package com.reactive.reactorstudy.reactor

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.slf4j.LoggerFactory

class MyPublisher: Publisher<Int> {

    private val log = LoggerFactory.getLogger(this.javaClass)

    val it = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    override fun subscribe(sub: Subscriber<in Int>) {
        log.info("구독자: 신문사 나 너희꺼 구독한다?")
        log.info("신문사: ㅇㅋ ㅇㅋ 구독정보 만들어서 곧 드림.. 기둘 ㅎ")
        val subscription = MySubscription(sub, it)
        log.info("신문사: 구독정보 생성 완료함.")
        sub.onSubscribe(subscription)
    }
}

