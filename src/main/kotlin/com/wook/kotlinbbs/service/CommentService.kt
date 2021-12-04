package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Comment
import com.wook.kotlinbbs.repository.CommentRepository
import org.springframework.data.repository.findByIdOrNull
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

    fun updateComment(id: Long, comment: Comment): Comment {
        return checkNotNull(commentRepository.findByIdOrNull(id)) { "삭제되었거나 없는 댓글입니다." }.change(comment)
    }
}