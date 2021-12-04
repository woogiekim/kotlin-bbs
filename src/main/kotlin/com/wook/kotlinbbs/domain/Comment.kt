package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_comment")
class Comment private constructor(
    var content: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_comment_to_board"))
    val board: Board,

    var author: String? = null
) : BaseEntity() {
    init {
        valid()
    }

    private fun valid() {
        requireContent(content)
    }

    private fun requireContent(content: String) {
        require(content.isNotBlank()) { "댓글 내용은 필수값입니다." }
    }

    fun change(comment: Comment): Comment {
        return apply {
            require(this.board == comment.board) { "다른 게시물의 댓글입니다." }
            this.content = comment.content
        }
    }

    companion object {
        fun createOf(author: String, content: String, board: Board): Comment {
            require(author.isNotBlank()) { "댓글 작성자는 필수값입니다." }
            return Comment(content, board, author)
        }

        fun updateOf(content: String, board: Board): Comment {
            return Comment(content, board)
        }
    }
}