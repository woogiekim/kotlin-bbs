package com.wook.kotlinbbs.presentation.dto

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Comment

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
    val content: String
) {

    companion object {
        fun fromEntity(comment: Comment): CommentResponse {
            return comment.run {
                CommentResponse(
                    checkNotNull(id) { "댓글 아이디가 없습니다." },
                    author,
                    content
                )
            }
        }
    }
}
