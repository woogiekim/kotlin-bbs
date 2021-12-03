package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class BoardService(private val boardRepository: BoardRepository) {

    fun addBoard(board: Board): Board {
        return boardRepository.save(board)
    }

    fun getBoards(pageable: Pageable): List<Board> {
        return boardRepository.findAll(pageable).content
    }

    fun getBoard(id: Long): Board {
        return boardRepository.findByIdOrNull(id) ?: throw IllegalStateException("삭제되었거나 없는 게시글입니다.")
    }
}