package com.hpursan.digicert.library.controller;

import com.hpursan.digicert.library.domain.Book;
import com.hpursan.digicert.library.exception.BookNotFoundException;
import com.hpursan.digicert.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void getAllBooks_whenNoneExist_shouldReturnNoContent() throws Exception {
        when (bookService.listAllBooks()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/books/listBooks"))
               .andExpect(status().isNoContent());
    }

    @Test
    public void getAllBooks_whenUnexpectedException_shouldReturnInternalServerError() throws Exception {
        when (bookService.listAllBooks()).thenThrow(new RuntimeException("Unexpected exception"));

        mockMvc.perform(get("/api/books/listBooks"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    public void getAllBooks_whenBooksExist_shouldReturnAllBooks() throws Exception {

        Book book1 = new Book(1L, "The Shining", "Stephen King", "1234");
        Book book2 = new Book(2L, "IT", "Stephen King", "4567");

        List<Book> bookList = new ArrayList<>(Arrays.asList(book1, book2));

        when(bookService.listAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/api/books/listBooks"))
            .andExpect(jsonPath("$.size()", is(bookList.size())))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is("The Shining")))
            .andExpect(jsonPath("$[0].author", is("Stephen King")))
            .andExpect(jsonPath("$[0].isbn", is("1234")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].title", is("IT")))
            .andExpect(jsonPath("$[1].author", is("Stephen King")))
            .andExpect(jsonPath("$[1].isbn", is("4567")))
            .andExpect(status().isOk());
    }

    @Test
    public void getBookById_whenBookExists_shouldReturnGivenBook() throws Exception {

        Book book = new Book(1L, "The Shining", "Stephen King", "1234");

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("The Shining"))
                .andExpect(jsonPath("$.author").value("Stephen King"))
                .andExpect(jsonPath("$.isbn").value("1234"));
    }

    @Test
    public void getBookById_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {

        when(bookService.getBookById(1L)).thenThrow(new BookNotFoundException("Book with id " + 1L + " not found"));

        mockMvc.perform(get("/api/books/{id}", 1))
            .andExpect(status().isNotFound());
    }

    @Test
    public void addBook_shouldAddBookAndReturnCreated() throws Exception{
        Book newlyCreatedBook = new Book(1L, "The Shining", "Stephen King", "1234");

        when(bookService.addBook(any(Book.class))).thenReturn(newlyCreatedBook);

        mockMvc.perform(post("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"The Shining\",\"author\":\"Stephen King\",\"isbn\":\"1234\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void updateBook_whenBookExists_shouldReturnOk() throws Exception {
        Book book = new Book(1L, "The Shining", "Stephen King", "1234");

        when(bookService.updateBook(any(Long.class), any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"The Shining\",\"author\":\"Stephen King\",\"isbn\":\"1234\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("The Shining"));
    }

    @Test
    public void updateBook_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {

        when(bookService.updateBook(any(Long.class), any(Book.class)))
            .thenThrow(new BookNotFoundException("Book with id " + 1L + " not found"));

        mockMvc.perform(put("/api/books/{id}",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"The Shining\",\"author\":\"Stephen King\",\"isbn\":\"1234\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBook_whenBookExists_shouldReturnOk() throws Exception {
        doNothing().when(bookService).deleteBook(any(Long.class));

        mockMvc.perform(delete("/api/books/{id}", 1L))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteBook_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {

        doThrow(new BookNotFoundException("Book with id " + 1L + " not found")).when(bookService).deleteBook(any(Long.class));

        mockMvc.perform(delete("/api/books/{id}",1L))
            .andExpect(status().isNotFound());
    }
}
