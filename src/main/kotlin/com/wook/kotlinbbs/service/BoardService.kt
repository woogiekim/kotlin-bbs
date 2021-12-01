package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import org.springframework.stereotype.Service

@Service
class BoardService(private val boardRepository: BoardRepository) {

    fun addBoard(board: Board): Board {
        return boardRepository.save(board)
    }
}