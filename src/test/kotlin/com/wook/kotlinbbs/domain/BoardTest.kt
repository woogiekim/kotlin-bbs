package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class BoardTest {

    @Test
    fun `작성자, 제목, 내용으로 게시글을 등록한다`() {
        //given
        val id = 1L
        val author = "김태욱"
        val title = "제목 테스트"
        val content = "내용 테스트"

        //when
        val board = Board(id = id, author = author, title = title, content = content)

        //then
        assertThat(board).isEqualTo(Board(id = id, author = author, title = title, content = content))
    }

    @Test
    fun `작성자, 제목, 내용이 없으면 예외가 발생한다`() {
        assertAll(
            {
                assertThatIllegalArgumentException()
                    .isThrownBy { Board(id = 1L, author = " ", title = "제목 테스트", content = "내용 테스트") }
                    .withMessageContaining("작성자")
            },
            {
                assertThatIllegalArgumentException()
                    .isThrownBy { Board(id = 1L, author = "김태욱", title = " ", content = "내용 테스트") }
                    .withMessageContaining("제목")
            },
            {
                assertThatIllegalArgumentException()
                    .isThrownBy { Board(id = 1L, author = "김태욱", title = "제목 테스트", content = " ") }
                    .withMessageContaining("내용")
            }
        )
    }
}