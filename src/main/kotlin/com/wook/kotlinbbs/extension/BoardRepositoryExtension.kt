package com.wook.kotlinbbs.extension

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.repository.BoardRepository

fun BoardRepository.findByIdAndDeletedIsFalseOrNull(id: Long): Board? = findByIdAndDeletedIsFalse(id).orElse(null)