package com.wook.kotlinbbs.presentation.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.wook.kotlinbbs.domain.Board
import java.time.LocalDateTime

data class BoardCreateRequest(
    val author: String,
    val title: String,
    val content: String
) {
    fun toEntity(): Board {
        return Board.createOf(author, title, content)
    }
}

data class BoardUpdateRequest(
    val title: String,
    val content: String
) {
    fun toEntity(): Board {
        return Board.updateOf(title, content)
    }
}

data class BoardResponse(
    val id: Long,
    val author: String,
    val title: String,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime?
) {

    companion object {
        fun fromEntity(board: Board): BoardResponse {
            return board.run {
                BoardResponse(
                    checkNotNull(id) { "게시물 아이디가 없습니다." },
                    checkNotNull(author) { "게시물 작성자가 없습니다." },
                    title,
                    content,
                    createAt,
                    updateAt
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