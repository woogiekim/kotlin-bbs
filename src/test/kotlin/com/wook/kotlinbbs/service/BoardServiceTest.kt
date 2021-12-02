package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.`when`
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.stream.LongStream
import kotlin.streams.toList

@DisplayName("게시판 서비스 테스트")
@ExtendWith(MockitoExtension::class)
class BoardServiceTest {

    val mockBoardRepository = mock(BoardRepository::class.java)
    val boardService = BoardService(mockBoardRepository)

    @DisplayName("게시물 등록")
    @Test
    fun addBoard() {
        //given
        val board = Board(id = 1L, author = "김태욱", title = "제목 테스트", content = "내용 테스트")
        `when`(mockBoardRepository.save(board)).thenReturn(board)

        //when
        val actualBoard = boardService.addBoard(board)

        //then
        assertThat(actualBoard).isEqualTo(board)
        verify(mockBoardRepository, times(1)).save(board)
    }

    @DisplayName("게시물 목록 조회")
    @Test
    fun getBoards() {
        //given
        val pageRequest = PageRequest.of(1, 10)
        val givenBoards = LongStream.range(1, 10)
            .mapToObj { Board(id = it, author = "김태욱 $it", title = "제목 테스트 $it", content = "내용 테스트 $it") }
            .toList()
        `when`(mockBoardRepository.findAll(pageRequest)).thenReturn(PageImpl(givenBoards))

        //when
        val boards = boardService.getBoards(pageRequest)

        //then
        assertThat(boards).isNotEmpty.isEqualTo(givenBoards)
    }
}