package com.reactive.reactorstudy.reactor

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.*

// 구독정보(구독자, 어떤 데이터를 구독할지)
class MySubscription: Subscription {

    private val subscriber: Subscriber<in Int>
    private val it: Iterator<Int>

     constructor(_subscriber: Subscriber<in Int>, _it: Iterable<Int>) {
        this.subscriber = _subscriber
        this.it = _it.iterator()
    }

    override fun request(n: Long) {
        var count = n

        while (count > 0) {
            if (it.hasNext()) {
                subscriber.onNext(it.next()) // 1
            } else {
                subscriber.onComplete()
                break
            }
            count = count.minus(1)
        }
    }

    override fun cancel() {

    }
}
