package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Comment
import com.wook.kotlinbbs.repository.CommentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class CommentService(private val commentRepository: CommentRepository) {

    fun addComment(board: Board, comment: Comment): Comment {
        return commentRepository.save(comment.withBoard(board))
    }
}