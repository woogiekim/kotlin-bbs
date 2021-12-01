package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_like")
class Like(
    var author: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_like_to_board"))
    val board: Board
) : BaseEntity()