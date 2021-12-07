package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Like
import com.wook.kotlinbbs.repository.LikeRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class LikeService(private val likeRepository: LikeRepository) {
    fun like(like: Like): Like {
        return likeRepository.save(like)
    }

    fun getLikes(boardId: Long): List<Like> {
        return likeRepository.findAllByBoardId(boardId)
    }

    fun unlike(id: Long) {
        likeRepository.deleteById(id)
    }
}
