package com.reactive.reactorstudy.todo.service

import com.reactive.reactorstudy.todo.entity.TodoItem
import com.reactive.reactorstudy.todo.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Transactional
@Service
class TodoItemService(
    private val todoRepository: TodoRepository
) {

    fun saveTodoItem(todoItem: TodoItem): Mono<TodoItem> {
        return todoRepository.save(todoItem)
    }

    fun getAllTodoItems(): Flux<TodoItem> {
        return todoRepository.findAll()
    }

    fun removeTodoItem(id: Long): Mono<Void> {
        return todoRepository.deleteById(id)
    }
}
