package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class CommentTest {

    @Test
    fun `given author, content, board when create comment then created`() {
        //given
        val author = "김태욱"
        val content = "댓글 내용"
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }

        //when
        val comment = Comment.createOf(author, content, board).apply { this.id = 1L }

        //then
        assertThat(comment).isNotNull.isEqualTo(Comment.createOf(author, content, board).apply { this.id = 1L })
    }

    @Test
    fun `given blank author when create comment then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException().isThrownBy {
            val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
            Comment.createOf(" ", "내용", board).apply { this.id = 1L }
        }
    }

    @Test
    fun `given blank content when create comment then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException().isThrownBy {
            val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
            Comment.createOf("김태욱", " ", board).apply { this.id = 1L }
        }
    }

    @Test
    fun `given content when update comment then updated`() {
        //given
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
        val comment = Comment.createOf("김태욱", "댓글 내용", board).apply { this.id = 1L }

        val givenComment = Comment.updateOf("수정", board)

        //when
        val updateComment = comment.change(givenComment)

        //then
        assertThat(updateComment).isNotNull
            .extracting(Comment::content)
            .isEqualTo(givenComment.content)
    }

    @Test
    fun `given blank content when update comment then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException().isThrownBy {
            val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
            Comment.updateOf(" ", board)
        }
    }
}