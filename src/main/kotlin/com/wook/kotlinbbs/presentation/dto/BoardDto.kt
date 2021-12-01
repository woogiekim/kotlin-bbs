package com.wook.kotlinbbs.presentation.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.wook.kotlinbbs.domain.Board

data class BoardRequest(
    val author: String,
    val title: String,
    val content: String
) {
    fun toEntity(): Board {
        return Board(author = author, title = title, content = content)
    }

    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

data class BoardResponse(
    val id: Long?,
    val author: String,
    val title: String,
    val content: String,
    val createAt: String,
    val updateAt: String
) {

    companion object {
        fun fromEntity(board: Board): BoardResponse {
            return board.run {
                BoardResponse(
                    id = id,
                    author = author,
                    title = title,
                    content = content,
                    createAt = createAt.toString(),
                    updateAt = updateAt.toString()
                )
            }
        }
    }
}