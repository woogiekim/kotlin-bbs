package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `작성자 제목 내용으로 게시글을 등록한다`() {
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
}