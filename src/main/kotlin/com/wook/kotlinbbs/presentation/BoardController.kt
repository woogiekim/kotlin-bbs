package com.wook.kotlinbbs.presentation

import com.wook.kotlinbbs.presentation.dto.*
import com.wook.kotlinbbs.service.BoardService
import com.wook.kotlinbbs.service.CommentService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/boards")
class BoardController(
    private val boardService: BoardService,
    private val commentService: CommentService
) {

    @PostMapping
    fun addBoard(@RequestBody boardCreateRequest: BoardCreateRequest): ResponseEntity<BoardResponse> {
        val boardResponse = BoardResponse.fromEntity(boardService.addBoard(boardCreateRequest.toEntity()))

        return ResponseEntity.created(URI.create("/boards/${boardResponse.id}")).body(boardResponse)
    }

    @GetMapping
    fun getBoards(pageable: Pageable): BoardResponses {
        return BoardResponses.fromEntity(boardService.getBoards(pageable))
    }

    @GetMapping("/{id}")
    fun getBoard(@PathVariable id: Long): BoardResponse {
        return BoardResponse.fromEntity(boardService.getBoard(id))
    }

    @PutMapping("/{id}")
    fun updateBoard(@PathVariable id: Long, @RequestBody boardUpdateRequest: BoardUpdateRequest): BoardResponse {
        return BoardResponse.fromEntity(boardService.updateBoard(id, boardUpdateRequest.toEntity()))
    }

    @DeleteMapping("/{id}")
    fun deleteBoard(@PathVariable id: Long): ResponseEntity<Any> {
        boardService.deleteBoard(id)

        return ResponseEntity.noContent().location(URI.create("/boards")).build()
    }

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

    @PutMapping("/{boardId}/comments/{id}")
    fun updateComment(
        @PathVariable boardId: Long,
        @PathVariable id: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest
    ): CommentResponse {
        return CommentResponse.fromEntity(
            commentService.updateComment(id, commentUpdateRequest.toEntityWith(boardService.getBoard(boardId)))
        )
    }
}