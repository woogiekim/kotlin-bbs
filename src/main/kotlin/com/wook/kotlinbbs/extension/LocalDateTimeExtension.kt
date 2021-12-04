package com.wook.kotlinbbs.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

infix fun LocalDateTime?.formatOrNull(formatter: DateTimeFormatter): String? {
    return this?.format(formatter)
}