package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_comment")
class Comment(
    var author: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_comment_to_board"))
    val board: Board
) : BaseEntity()