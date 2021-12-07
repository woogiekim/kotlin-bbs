package com.wook.kotlinbbs.service

import com.wook.kotlinbbs.domain.Board
import com.wook.kotlinbbs.domain.Like
import com.wook.kotlinbbs.repository.LikeRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.stream.IntStream
import kotlin.streams.toList

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
        val givenLike: Like = Like("김태욱", Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }).apply { this.id = 1L }

        //when
        every { mockLikeRepository.save(givenLike) } returns givenLike

        val like = likeService.like(givenLike)

        //then
        assertThat(like).isEqualTo(givenLike)
        verify { mockLikeRepository.save(givenLike) }
    }

    @Test
    fun getLikes() {
        //given
        val board = Board.createOf("김태욱", "제목", "내용").apply { this.id = 1L }
        val givenLikes = IntStream.range(1, 11)
            .mapToObj { Like("김태욱 $it", board).apply { this.id = it.toLong() } }
            .toList()

        //when
        every { board.id?.let { mockLikeRepository.findAllByBoardId(it) } } returns givenLikes

        val likes = board.id?.let { likeService.getLikes(it) }

        //then
        assertThat(likes).isNotEmpty.hasSize(10).isEqualTo(givenLikes)
        verify { board.id?.let { mockLikeRepository.findAllByBoardId(it) } }
    }
}