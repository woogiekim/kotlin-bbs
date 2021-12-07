package com.wook.kotlinbbs.presentation

import com.wook.kotlinbbs.presentation.dto.LikeRequest
import com.wook.kotlinbbs.presentation.dto.LikeResponse
import com.wook.kotlinbbs.presentation.dto.LikeResponses
import com.wook.kotlinbbs.service.BoardService
import com.wook.kotlinbbs.service.LikeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/boards")
class LikeController(
    private val boardService: BoardService,
    private val likeService: LikeService
) {
    @PostMapping("/{boardId}/likes")
    fun like(@PathVariable boardId: Long, @RequestBody likeRequest: LikeRequest): ResponseEntity<LikeResponse> {
        val likeResponse =
            LikeResponse.fromEntity(likeService.like(likeRequest.toEntityWith(boardService.getBoard(boardId))))

        return ResponseEntity.created(URI.create("/$boardId/likes")).body(likeResponse)
    }

    @GetMapping("/{boardId}/likes")
    fun getLikes(@PathVariable boardId: Long): LikeResponses {
        return LikeResponses.fromEntity(likeService.getLikes(boardId))
    }

    @DeleteMapping("/{boardId}/likes/{id}")
    fun unlike(@PathVariable boardId: Long, @PathVariable id: Long): ResponseEntity<Any> {
        likeService.unlike(id)

        return ResponseEntity.noContent().location(URI.create("/$boardId/likes")).build()
    }
}