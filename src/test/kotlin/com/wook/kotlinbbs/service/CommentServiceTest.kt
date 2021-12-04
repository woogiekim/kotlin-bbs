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
}