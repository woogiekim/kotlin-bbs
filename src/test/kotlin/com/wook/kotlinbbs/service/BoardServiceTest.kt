package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@DisplayName("게시판 서비스 테스트")
@ExtendWith(MockitoExtension::class)
class BoardServiceTest {

    @DisplayName("게시물 등록")
    @Test
    fun addBoard() {
        //given
        val mockBoardRepository = mock(BoardRepository::class.java)
        val boardService = BoardService(mockBoardRepository)

        val board = Board(id = 1L, author = "김태욱", title = "제목 테스트", content = "내용 테스트")
        `when`(mockBoardRepository.save(board)).thenReturn(board)

        //when
        val actualBoard = boardService.addBoard(board)

        //then
        assertThat(actualBoard).isEqualTo(board)
        verify(mockBoardRepository, times(1)).save(board)
    }
}