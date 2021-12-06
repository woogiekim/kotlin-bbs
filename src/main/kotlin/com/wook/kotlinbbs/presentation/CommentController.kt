package com.wook.kotlinbbs.presentation

import com.wook.kotlinbbs.presentation.dto.CommentCreateRequest
import com.wook.kotlinbbs.presentation.dto.CommentResponse
import com.wook.kotlinbbs.presentation.dto.CommentResponses
import com.wook.kotlinbbs.presentation.dto.CommentUpdateRequest
import com.wook.kotlinbbs.service.BoardService
import com.wook.kotlinbbs.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/boards")
class CommentController(
    private val commentService: CommentService,
    private val boardService: BoardService
) {
    @PostMapping("/{boardId}/comments")
    fun addComment(
        @PathVariable boardId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {
        val commentResponse = CommentResponse.fromEntity(
            commentService.addComment(commentCreateRequest.toEntityWith(boardService.getBoard(boardId)))
        )
        return ResponseEntity.created(URI.create("/boards/$boardId/comments")).body(commentResponse)
    }

    @GetMapping("/{boardId}/comments")
    fun getComments(@PathVariable boardId: Long): CommentResponses {
        return CommentResponses.fromEntity(commentService.getComments(boardId))
    }

    @PutMapping("/comments/{id}")
    fun updateComment(
        @PathVariable id: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest
    ): CommentResponse {
        return CommentResponse.fromEntity(commentService.updateComment(id, commentUpdateRequest.toEntityWith()))
    }

    @DeleteMapping("/comments/{id}")
    fun deleteComment(@PathVariable id: Long): ResponseEntity<Any> {
        commentService.deleteComment(id)

        return ResponseEntity.noContent().build()
    }
}