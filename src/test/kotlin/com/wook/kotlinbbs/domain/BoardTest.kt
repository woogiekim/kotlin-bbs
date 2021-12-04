package com.wook.kotlinbbs.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `given id, author, title and content when create Board then created`() {
        //given
        val id = 1L
        val author = "김태욱"
        val title = "제목 테스트"
        val content = "내용 테스트"

        //when
        val board = Board.createOf(author, title, content).apply { this.id = id }

        //then
        assertThat(board).isEqualTo(Board.createOf(author, title, content).apply { this.id = id })
    }

    @Test
    fun `given blank author when create board then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board.createOf(" ", "제목", "내용") }
            .withMessageContaining("작성자")
    }

    @Test
    fun `given blank title when create board then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board.createOf("김태욱", " ", "내용") }
            .withMessageContaining("제목")
    }

    @Test
    fun `given content author when create board then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Board.createOf("김태욱", "제목", " ") }
            .withMessageContaining("내용")
    }

    @Test
    fun `given title and content when change board then changed`() {
        //given
        //게시물 생성되어있음
        val id = 1L
        val author = "김태욱"
        val board = Board.createOf(author, "제목", "내용").apply { this.id = id }

        val changeTitle = "제목 수정"
        val changeContent = "내용 수정"
        val updateBoard = Board.updateOf(changeTitle, changeContent)

        //when
        val changedBoard = board.change(updateBoard)

        //then
        assertThat(changedBoard).isNotNull
            .extracting(Board::id, Board::author, Board::title, Board::content)
            .containsExactly(id, author, changeTitle, changeContent)
    }

    @Test
    fun `given blank title when change board then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException()
            .isThrownBy {
                Board.createOf("김태욱", "제목", "내용")
                    .change(Board.updateOf(" ", "내용 수정"))
            }
            .withMessageContaining("제목")
    }

    @Test
    fun `given blank content when change board then throw IllegalArgumentException`() {
        assertThatIllegalArgumentException()
            .isThrownBy {
                Board.createOf("김태욱", "제목", "내용")
                    .change(Board.updateOf("제목 수정", " "))
            }
            .withMessageContaining("내용")
    }

    @Test
    fun `given id when delete board then deleted`() {
        //given
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }

        //when
        board.delete()

        //then
        assertThat(board.deleted).isTrue
    }
}