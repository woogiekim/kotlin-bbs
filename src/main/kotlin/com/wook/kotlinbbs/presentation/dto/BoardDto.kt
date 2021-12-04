package com.wook.kotlinbbs.presentation.dto

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.extension.dateFormatter
import com.wook.kotlinbbs.extension.formatOrNull

data class BoardRequest(
    val author: String,
    val title: String,
    val content: String
) {
    fun toEntity(): Board {
        return Board(author, title, content)
    }
}

data class BoardResponse(
    val id: Long,
    val author: String,
    val title: String,
    val content: String,
    val createAt: String?,
    val updateAt: String?
) {

    companion object {
        fun fromEntity(board: Board): BoardResponse {
            return board.run {
                BoardResponse(
                    id!!,
                    author,
                    title,
                    content,
                    createAt.formatOrNull(dateFormatter),
                    updateAt.formatOrNull(dateFormatter)
                )
            }
        }
    }
}

data class BoardResponses(
    val boardResponses: List<BoardResponse>
) {
    companion object {
        fun fromEntity(boards: List<Board>): BoardResponses {
            return boards.run {
                BoardResponses(map { BoardResponse.fromEntity(it) }.toList())
            }
        }
    }
}