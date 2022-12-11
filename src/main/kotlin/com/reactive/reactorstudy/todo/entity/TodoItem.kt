package com.reactive.reactorstudy.todo.entity

import com.reactive.reactorstudy.global.common.EntityAuditing
import com.reactive.reactorstudy.todo.dto.TodoDto

class TodoItem(
    var text: String,
    var isDone: Boolean
): EntityAuditing() {

    companion object {
        fun createTodoItem(todoDto: TodoDto): TodoItem {
            return TodoItem(todoDto.text, todoDto.isDone)
        }
    }

    override fun toString(): String {
        return "TodoItem(text='$text', isDone=$isDone)"
    }
}

