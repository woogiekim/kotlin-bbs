package com.wook.kotlinbbs.repository

import com.wook.kotlinbbs.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBoardIdAndDeletedIsFalse(id: Long): List<Comment>

    fun findByIdAndDeletedIsFalse(id: Long): Comment?
}
