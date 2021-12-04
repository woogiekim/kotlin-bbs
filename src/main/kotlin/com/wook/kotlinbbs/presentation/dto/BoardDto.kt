package com.wook.kotlinbbs.presentation.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
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
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val createAt: LocalDateTime?,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
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
                BoardResponses(map { BoardResponse.fromEntity(it) })
            }
        }
    }
}