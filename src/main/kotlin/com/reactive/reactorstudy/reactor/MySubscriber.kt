package com.reactive.reactorstudy.reactor

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory

class MySubscriber : Subscriber<Int> {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private lateinit var subscription: Subscription
    private var bufferSize: Long = 2

    override fun onSubscribe(subscription: Subscription) {
        log.info("구독자: 구독정보 잘받았음.")
        this.subscription = subscription
        log.info("구독자: 나 이제 신문 1개씩 줘")
        subscription.request(2) // 신문 2개씩 발행 요청 - 한번에 받을 개수
    }

    override fun onNext(t: Int?) {
        log.info("구독 데이터 전달 onNext(): $t")
        bufferSize = bufferSize.minus(1)

        if (bufferSize.compareTo(0) == 0) {
            log.info("하루지남")
            bufferSize = bufferSize.plus(2)
            subscription.request(bufferSize)
        }
    }

    override fun onError(t: Throwable?) {
        log.error("구독 중 에러!")
    }

    override fun onComplete() {
        log.info("구독 완료!")
    }
}
