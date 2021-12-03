package com.wook.kotlinbbs.presentation

import com.wook.kotlinbbs.presentation.dto.BoardRequest
import com.wook.kotlinbbs.presentation.dto.BoardResponse
import com.wook.kotlinbbs.presentation.dto.BoardResponses
import com.wook.kotlinbbs.service.BoardService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @PostMapping
    fun addBoard(@RequestBody boardRequest: BoardRequest): ResponseEntity<BoardResponse> {
        val boardResponse = BoardResponse.fromEntity(boardService.addBoard(boardRequest.toEntity()))

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
}