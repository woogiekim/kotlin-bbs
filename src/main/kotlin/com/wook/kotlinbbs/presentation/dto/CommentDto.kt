package com.wook.kotlinbbs.presentation.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Comment
import java.time.LocalDateTime

data class CommentCreateRequest(
    val author: String,
    val content: String
) {
    fun toEntityWith(board: Board): Comment {
        return Comment.createOf(author, content, board)
    }
}

data class CommentResponse(
    val id: Long,
    val author: String,
    val content: String,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val createAt: LocalDateTime?,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val updateAt: LocalDateTime?
) {
    companion object {
        fun fromEntity(comment: Comment): CommentResponse {
            return comment.run {
                CommentResponse(
                    checkNotNull(id) { "댓글 아이디가 없습니다." },
                    author,
                    content,
                    createAt,
                    updateAt
                )
            }
        }
    }
}

data class CommentResponses(
    val commentResponses: List<CommentResponse>
) {
    companion object {
        fun fromEntity(comments: List<Comment>): CommentResponses {
            return comments.run {
                CommentResponses(map { CommentResponse.fromEntity(it) })
            }
        }
    }
}
