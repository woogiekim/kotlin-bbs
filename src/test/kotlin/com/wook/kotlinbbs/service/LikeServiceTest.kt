package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Like
import com.wook.kotlinbbs.repository.LikeRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("댓글 서비스 테스트")
@ExtendWith(MockKExtension::class)
class LikeServiceTest {
    @MockK
    private lateinit var mockLikeRepository: LikeRepository

    @InjectMockKs
    private lateinit var likeService: LikeService

    @Test
    fun like() {
        //given
        val like: Like = Like("김태욱", Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }).apply { this.id = 1L }

        //when
        every { mockLikeRepository.save(like) } returns like
        //then
        assertThat(like)
    }
}