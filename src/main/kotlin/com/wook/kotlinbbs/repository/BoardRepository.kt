package com.wook.kotlinbbs.repository

import com.wook.kotlinbbs.domain.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long>