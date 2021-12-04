package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.extension.findByIdAndDeletedIsFalseOrNull
import com.wook.kotlinbbs.repository.BoardRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class BoardService(private val boardRepository: BoardRepository) {

    fun addBoard(board: Board): Board {
        return boardRepository.save(board)
    }

    fun getBoards(pageable: Pageable): List<Board> {
        return boardRepository.findAllByDeletedIsFalse(pageable).content
    }

    fun getBoard(id: Long): Board {
        return boardRepository.findByIdAndDeletedIsFalseOrNull(id) ?: throw IllegalStateException("삭제되었거나 없는 게시글입니다.")
    }

    fun updateBoard(id: Long, board: Board): Board {
        return this.getBoard(id).change(board)
    }

    fun deleteBoard(id: Long) {
        this.getBoard(id).delete()
    }
}