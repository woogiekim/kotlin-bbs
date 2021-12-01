package com.wook.kotlinbbs.domain

import javax.persistence.Entity

@Entity
class Board(
    id: Long? = null,
    val author: String,
    var title: String,
    var content: String
) : BaseEntity(id)