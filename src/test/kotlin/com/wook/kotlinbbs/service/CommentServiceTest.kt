package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Comment
import com.wook.kotlinbbs.repository.CommentRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import java.util.stream.LongStream
import kotlin.streams.toList

@DisplayName("댓글 서비스 테스트")
@ExtendWith(MockKExtension::class)
class CommentServiceTest {

    @MockK
    private lateinit var mockCommentRepository: CommentRepository

    @InjectMockKs
    private lateinit var commentService: CommentService

    @DisplayName("댓글 등록")
    @Test
    fun addComment() {
        //given
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
        val comment = Comment.createOf("김태욱", "댓글 내용", board)
        every { mockCommentRepository.save(comment) } returns comment

        //when
        val addComment = commentService.addComment(comment)

        //then
        assertThat(addComment).isNotNull.isEqualTo(comment)
        verify { mockCommentRepository.save(comment) }
    }

    @DisplayName("댓글 목록 조회")
    @Test
    fun getComments() {
        //given
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
        val comments = LongStream.range(1, 10)
            .mapToObj { Comment.createOf("김태욱", "내용", board).apply { this.id = it } }
            .toList()
        every { board.id?.let { mockCommentRepository.findAllByBoardId(it) } } returns comments

        //when
        val findAllComments = board.id?.let { commentService.getComments(it) }

        //then
        assertThat(findAllComments).isNotEmpty.isEqualTo(comments)
        verify { board.id?.let { mockCommentRepository.findAllByBoardId(it) } }
    }

    @DisplayName("댓글 수정")
    @Test
    fun updateComment() {
        //given
        val id = 1L
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
        val findComment = Comment.createOf("김태욱", "내용", board).apply { this.id = 1L }
        every { mockCommentRepository.findByIdOrNull(id) } returns findComment

        val givenComment = Comment.updateOf("내용 수정", board)

        //when
        val updateComment = commentService.updateComment(id, givenComment)

        //then
        assertThat(updateComment).isNotNull
            .extracting(Comment::content)
            .isEqualTo(givenComment.content)
        verify { mockCommentRepository.findByIdOrNull(id) }
    }
}