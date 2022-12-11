package com.reactive.reactorstudy.todo.api

import com.reactive.reactorstudy.todo.dto.TodoDto
import com.reactive.reactorstudy.todo.entity.TodoItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/*@RequestMapping("/api/v1")
@RestController
class TodoApi(
    private val todoItemService: TodoItemService
) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/todos")
    fun saveTodoItem(@RequestBody todoDto: TodoDto): Mono<TodoItem> {
        log.info("TodoDto: $todoDto")
        val todoItem = todoDto.toEntity()
        return todoItemService.saveTodoItem(todoItem)
    }

    @GetMapping("/todos")
    fun getAllTodoItems(): Flux<TodoItem> {
        log.info("TodoItems 가져오기!!")
        return todoItemService.getAllTodoItems()
    }

    @DeleteMapping("todos/{id}")
    fun removeTodoItem(@PathVariable("id") id: Long): Mono<Void> {
        log.info("id: $id")
        return todoItemService.removeTodoItem(id)
    }
}*/
