package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.extension.findByIdAndDeletedIsFalseOrNull
import com.wook.kotlinbbs.presentation.dto.BoardUpdateRequest
import com.wook.kotlinbbs.repository.BoardRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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
        val board = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = 1L }
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
            .mapToObj { Board.createOf("김태욱 $it", "제목 테스트 $it", "내용 테스트 $it").apply { this.id = it } }
            .toList()
        every { mockBoardRepository.findAllByDeletedIsFalse(pageRequest) } returns PageImpl(givenBoards)

        //when
        val boards = boardService.getBoards(pageRequest)

        //then
        assertThat(boards).isNotEmpty.isEqualTo(givenBoards)
        verify { mockBoardRepository.findAllByDeletedIsFalse(pageRequest) }
    }

    @DisplayName("게시물 조회")
    @Test
    fun getBoard() {
        //given
        val id = 1L
        val givenBoard = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns givenBoard

        //when
        val actualBoard = boardService.getBoard(id)

        //then
        assertThat(actualBoard).isNotNull.isEqualTo(givenBoard)
        verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
    }

    @DisplayName("게시물을 찾을 수 없음")
    @Test
    fun notFoundBoard() {
        //given
        val id = 1L
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns null

        //when then
        assertThatIllegalStateException().isThrownBy {
            boardService.getBoard(id)
            verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
        }
    }

    @DisplayName("게시물 수정")
    @Test
    fun updateBoard() {
        //given
        val id = 1L
        val findBoard = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns findBoard

        val givenBoard = BoardUpdateRequest("제목 수정", "내용 수정").toEntity()

        //when
        val updateBoard = boardService.updateBoard(id, givenBoard)

        //then
        assertThat(updateBoard).isNotNull
            .extracting(Board::title, Board::content)
            .containsExactly(givenBoard.title, givenBoard.content)
    }

    @DisplayName("게시물을 수정할 때 게시물 제목은 필수로 입력해야함")
    @Test
    fun `given blank title when update board then throw IllegalArgumentException`() {
        //given
        val id = 1L
        val findBoard = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns findBoard

        //when then
        assertThatIllegalArgumentException().isThrownBy {
            boardService.updateBoard(id, BoardUpdateRequest(" ", "내용 수정").toEntity())
            verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
        }
    }

    @DisplayName("게시물을 수정할 때 게시물 내용은 필수로 입력해야함")
    @Test
    fun `given blank content when update board then throw IllegalArgumentException`() {
        //given
        val id = 1L
        val findBoard = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns findBoard

        //when then
        assertThatIllegalArgumentException().isThrownBy {
            boardService.updateBoard(id, BoardUpdateRequest("제목 수정", " ").toEntity())
            verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
        }
    }

    @DisplayName("게시물 삭제")
    @Test
    fun deleteBoard() {
        //given
        val id = 1L
        val findBoard = Board.createOf("김태욱", "제목 테스트", "내용 테스트").apply { this.id = id }
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns findBoard

        //when
        val deleted = boardService.deleteBoard(id).run { findBoard.deleted }

        //then
        assertThat(deleted).isTrue
        verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
    }

    @DisplayName("없거나 삭제 된 게시물은 삭제할 수 없음")
    @Test
    fun `given not exists id when delete board then throw IllegalStateException`() {
        //given
        val id = 1L
        every { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) } returns null

        //when then
        assertThatIllegalStateException().isThrownBy {
            boardService.deleteBoard(id)
            verify { mockBoardRepository.findByIdAndDeletedIsFalseOrNull(id) }
        }
    }
}