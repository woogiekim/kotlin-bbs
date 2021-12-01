package com.wook.kotlinbbs.domain

import javax.persistence.Entity

@Entity
class Board(
    private var author: String,
    private var title: String,
    private var content: String
) : BaseEntity()