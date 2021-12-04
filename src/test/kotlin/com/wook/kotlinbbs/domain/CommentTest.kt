package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
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
}