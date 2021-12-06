package com.wook.kotlinbbs.presentation.dto

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Like

data class LikeRequest(
    val author: String
) {
    fun toEntityWith(board: Board): Like {
        return Like(author, board)
    }
}

data class LikeResponse(
    val author: String,
    val board: Board
) {
    companion object {
        fun fromEntity(like: Like): LikeResponse {
            return like.run {
                LikeResponse(author, board)
            }
        }
    }
}