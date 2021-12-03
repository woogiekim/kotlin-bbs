package com.wook.kotlinbbs.domain

import javax.persistence.Entity

@Entity
class Board(
    val author: String,
    var title: String,
    var content: String
) : BaseEntity() {
    init {
        valid()
    }

    private fun valid() {
        require(author.isNotBlank()) { "게시물 작성자는 필수값입니다." }
        require(title.isNotBlank()) { "게시물 제목은 필수값입니다." }
        require(content.isNotBlank()) { "게시물 내용은 필수값입니다." }
    }
}