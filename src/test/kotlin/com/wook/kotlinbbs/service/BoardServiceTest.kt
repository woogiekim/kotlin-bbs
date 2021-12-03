package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import java.util.stream.LongStream
import kotlin.streams.toList

@DisplayName("게시판 서비스 테스트")
@ExtendWith(MockKExtension::class)
class BoardServiceTest {
    @MockK
    private lateinit var mockBoardRepository: BoardRepository

    @InjectMockKs
    private lateinit var boardService: BoardService

    @DisplayName("게시물 등록")
    @Test
    fun addBoard() {
        //given
        val board = Board("김태욱", "제목 테스트", "내용 테스트").apply { this.id = 1L }
        every { mockBoardRepository.save(board) } returns board

        //when
        val actualBoard = boardService.addBoard(board)

        //then
        assertThat(actualBoard).isEqualTo(board)
        verify { mockBoardRepository.save(board) }
    }

    @DisplayName("게시물 목록 조회")
    @Test
    fun getBoards() {
        //given
        val pageRequest = PageRequest.of(1, 10)
        val givenBoards = LongStream.range(1, 10)
            .mapToObj { Board("김태욱 $it", "제목 테스트 $it", "내용 테스트 $it").apply { this.id = it } }
            .toList()
        every { mockBoardRepository.findAll(pageRequest) } returns PageImpl(givenBoards)

        //when
        val boards = boardService.getBoards(pageRequest)

        //then
        assertThat(boards).isNotEmpty.isEqualTo(givenBoards)
        verify { mockBoardRepository.findAll(pageRequest) }
    }

    @DisplayName("게시물 조회")
    @Test
    fun getBoard() {
        //given
        val id = 1L
        val givenBoard = Board("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdOrNull(id) } returns givenBoard

        //when
        val actualBoard = boardService.getBoard(id)

        //then
        assertThat(actualBoard).isNotNull.isEqualTo(givenBoard)
        verify { mockBoardRepository.findByIdOrNull(id) }
    }

    @DisplayName("게시물을 찾을 수 없음")
    @Test
    fun notFoundBoard() {
        //given
        val id = 1L
        every { mockBoardRepository.findByIdOrNull(id) } returns null

        //when then
        assertThatIllegalStateException().isThrownBy { boardService.getBoard(id) }
        verify { mockBoardRepository.findByIdOrNull(id) }
    }
}