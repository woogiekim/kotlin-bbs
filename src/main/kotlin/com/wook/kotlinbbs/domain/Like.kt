package com.wook.kotlinbbs.domain

import javax.persistence.*

@Entity
@Table(name = "board_like")
class Like(
    private var author: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_like_to_board"))
    private var board: Board
) : BaseEntity()