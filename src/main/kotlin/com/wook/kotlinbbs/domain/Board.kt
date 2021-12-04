package com.wook.kotlinbbs.domain

import javax.persistence.Entity

@Entity
class Board private constructor(
    var title: String,
    var content: String
) : BaseEntity() {
    var author: String? = null
    var deleted: Boolean = false

    init {
        valid()
    }

    private fun valid() {
        requireTitle(title)
        requireContent(content)
    }

    private fun requireTitle(title: String) {
        require(title.isNotBlank()) { "게시물 제목은 필수값입니다." }
    }

    private fun requireContent(content: String) {
        require(content.isNotBlank()) { "게시물 내용은 필수값입니다." }
    }

    fun change(board: Board): Board {
        return this.apply {
            requireTitle(board.title)
            requireContent(board.content)

            this.title = board.title
            this.content = board.content
        }
    }

    fun delete() {
        deleted = true
    }

    companion object {
        fun createOf(author: String, title: String, content: String): Board {
            require(author.isNotBlank()) { "게시물 작성자는 필수값입니다." }
            return Board(title, content).apply { this.author = author }
        }

        fun updateOf(title: String, content: String): Board {
            return Board(title, content)
        }
    }
}