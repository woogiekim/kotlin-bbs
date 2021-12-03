package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.`when`
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
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
        val board = Board("김태욱", "제목 테스트", "내용 테스트").apply { this.id = 1L }
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
            .mapToObj { Board("김태욱 $it", title = "제목 테스트 $it", content = "내용 테스트 $it").apply { this.id = it } }
            .toList()
        `when`(mockBoardRepository.findAll(pageRequest)).thenReturn(PageImpl(givenBoards))

        //when
        val boards = boardService.getBoards(pageRequest)

        //then
        assertThat(boards).isNotEmpty.isEqualTo(givenBoards)
    }

    @DisplayName("게시물 조회")
    @Test
    fun getBoard() {
        //given
        val id = 1L
        val givenBoard = Board("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        `when`(mockBoardRepository.findById(id)).thenReturn(Optional.of(givenBoard))

        //when
        val actualBoard = boardService.getBoard(1L)

        //then
        assertThat(actualBoard).isNotNull.isEqualTo(givenBoard)
    }

    @DisplayName("게시물을 찾을 수 없음")
    @Test
    fun notFoundBoard() {
        //given
        val id = 1L
        val givenBoard = Board("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        `when`(mockBoardRepository.findById(id)).thenReturn(Optional.of(givenBoard))

        //when then
        assertThatIllegalStateException().isThrownBy { boardService.getBoard(2L) }
    }
}