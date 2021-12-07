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
    val id: Long,
    val author: String
) {
    companion object {
        fun fromEntity(like: Like): LikeResponse {
            return like.run {
                LikeResponse(
                    checkNotNull(id) { "좋아요 아이디가 없습니다." },
                    author
                )
            }
        }
    }
}

data class LikeResponses(
    val likeResponses: List<LikeResponse>
) {
    companion object {
        fun fromEntity(likes: List<Like>): LikeResponses {
            return likes.run {
                LikeResponses(map { LikeResponse.fromEntity(it) })
            }
        }
    }
}