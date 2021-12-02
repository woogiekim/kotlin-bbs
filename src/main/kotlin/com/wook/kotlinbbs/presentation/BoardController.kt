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
        return BoardResponse.fromEntity(boardService.addBoard(boardRequest.toEntity())).run {
            ResponseEntity.created(URI.create("/boards/$id")).body(this)
        }
    }

    @GetMapping
    fun getBoards(pageable: Pageable): ResponseEntity<BoardResponses> {
        return BoardResponses.fromEntity(boardService.getBoards(pageable)).run {
            ResponseEntity.ok(this)
        }
    }

    @GetMapping("/{id}")
    fun getBoard(@PathVariable id: Long): ResponseEntity<BoardResponse> {
        return BoardResponse.fromEntity(boardService.getBoard(id)).run {
            ResponseEntity.ok(this)
        }
    }
}