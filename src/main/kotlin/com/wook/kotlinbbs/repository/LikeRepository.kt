package com.wook.kotlinbbs.repository

import com.wook.kotlinbbs.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long>
