package com.wook.kotlinbbs.repository

import com.wook.kotlinbbs.domain.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardRepository : JpaRepository<Board, Long> {
    fun findAllByDeletedIsFalse(pageable: Pageable): Page<Board>

    fun findByIdAndDeletedIsFalse(id: Long): Optional<Board>
}