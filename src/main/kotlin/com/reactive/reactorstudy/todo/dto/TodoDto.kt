package com.reactive.reactorstudy.todo.dto

import com.reactive.reactorstudy.todo.entity.TodoItem

data class TodoDto(
    var text: String,
    var isDone: Boolean
) {

    fun toEntity(): TodoItem {
            return TodoItem.createTodoItem(this)
    }
}
