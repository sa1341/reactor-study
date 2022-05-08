package com.reactive.reactorstudy.todo.repository

import com.reactive.reactorstudy.todo.entity.TodoItem
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository: R2dbcRepository<TodoItem, Long> {

}
