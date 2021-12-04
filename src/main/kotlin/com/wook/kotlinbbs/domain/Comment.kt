package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_comment")
class Comment(
    val author: String,
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_comment_to_board"))
    var board: Board? = null
) : BaseEntity() {

    fun withBoard(board: Board): Comment {
        return this.apply { this.board = board }
    }
}