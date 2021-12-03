package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `작성자, 제목, 내용으로 게시글을 등록한다`() {
        //given
        val id = 1L
        val author = "김태욱"
        val title = "제목 테스트"
        val content = "내용 테스트"

        //when
        val board = Board(author, title, content).apply { this.id = id }

        //then
        assertThat(board).isEqualTo(Board(author, title, content).apply { this.id = id })
    }

    @Test
    fun `작성자가 없으면 예외가 발생한다`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board(" ", "제목", "내용") }
            .withMessageContaining("작성자")
    }

    @Test
    fun `제목이 없으면 예외가 발생한다`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board("김태욱", " ", "내용") }
            .withMessageContaining("제목")
    }

    @Test
    fun `내용이 없으면 예외가 발생한다`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board("김태욱", "제목", " ") }
            .withMessageContaining("내용")
    }
}