package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Comment
import com.wook.kotlinbbs.repository.CommentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class CommentService(private val commentRepository: CommentRepository) {
    fun addComment(comment: Comment): Comment {
        return commentRepository.save(comment)
    }

    fun getComments(id: Long): List<Comment> {
        return commentRepository.findAllByBoardId(id)
    }
}