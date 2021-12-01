package com.wook.kotlinbbs.presentation

import com.wook.kotlinbbs.presentation.dto.BoardRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@DisplayName("게시판 API 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `게시물 등록`() {
        val author = "김태욱"
        val title = "제목 테스트"
        val content = "내용 테스트"
        val boardRequest = BoardRequest(author = author, title = title, content = content)

        mockMvc
            .perform(
                post("/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(boardRequest.toJson())
            )
            .andExpect(status().isCreated)
            .andExpect(header().exists("location"))
            .andExpectAll(
                { jsonPath("$.id").exists() },
                { jsonPath("$.author", { it.equals(author) }) },
                { jsonPath("$.title", { it.equals(title) }) },
                { jsonPath("$.content", { it.equals(content) }) },
                { jsonPath("$.createAt").exists() },
                { jsonPath("$.updateAt").exists() }
            )
    }
}