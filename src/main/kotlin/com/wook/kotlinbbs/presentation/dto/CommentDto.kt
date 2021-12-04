package com.wook.kotlinbbs.presentation.dto

import com.wook.kotlinbbs.domain.Comment

data class CommentCreateRequest(
    val author: String,
    val content: String
) {
    fun toEntity(): Comment {
        return Comment(author, content)
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
                CommentResponse(id!!, author, content)
            }
        }
    }
}
