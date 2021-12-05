package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_comment")
class Comment private constructor(
    var content: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_comment_to_board"))
    var board: Board? = null,
    var author: String? = null,
    var deleted: Boolean = false
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
            this.content = comment.content
        }
    }

    fun delete() {
        deleted = true
    }

    companion object {
        fun createOf(author: String, content: String, board: Board): Comment {
            require(author.isNotBlank()) { "댓글 작성자는 필수값입니다." }
            return Comment(content, board, author)
        }

        fun updateOf(content: String): Comment {
            return Comment(content)
        }
    }
}