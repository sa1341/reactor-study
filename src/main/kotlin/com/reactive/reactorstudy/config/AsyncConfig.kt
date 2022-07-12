package com.reactive.reactorstudy.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig: AsyncConfigurerSupport() {

    override fun getAsyncExecutor(): Executor {

        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize  = 5
        executor.maxPoolSize = 10
        executor.setQueueCapacity(10)
        executor.setThreadNamePrefix("JEAN-CALM-")
        executor.initialize()

        return executor
    }
}
