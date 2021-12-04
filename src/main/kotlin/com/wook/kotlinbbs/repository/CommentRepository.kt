package com.wook.kotlinbbs.repository

import com.wook.kotlinbbs.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByBoardId(id: Long): List<Comment>
}
