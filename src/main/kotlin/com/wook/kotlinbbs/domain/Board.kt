package com.wook.kotlinbbs.domain

import javax.persistence.Entity

@Entity
class Board(
    var author: String,
    var title: String,
    var content: String
) : BaseEntity()