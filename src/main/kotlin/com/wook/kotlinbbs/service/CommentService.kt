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
        return commentRepository.findAllByBoardIdAndDeletedIsFalse(id)
    }

    fun updateComment(id: Long, comment: Comment): Comment {
        return getComment(id).change(comment)
    }

    fun deleteComment(id: Long) {
        getComment(id).delete()
    }

    private fun getComment(id: Long): Comment {
        return checkNotNull(commentRepository.findByIdAndDeletedIsFalse(id)) { "삭제되었거나 없는 댓글입니다." }
    }
}