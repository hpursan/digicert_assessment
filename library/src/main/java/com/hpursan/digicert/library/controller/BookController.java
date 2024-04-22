package com.hpursan.digicert.library.controller;

import com.hpursan.digicert.library.domain.Book;
import com.hpursan.digicert.library.exception.BookNotFoundException;
import com.hpursan.digicert.library.service.BookService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "List books", description = "An endpoint to get a list of books", responses = {
        @ApiResponse(description = "Success", responseCode = "200"),
        @ApiResponse(description = "No content", responseCode = "204")
    })
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author" ,required = false) String author) {
        List<Book> allBooks;

        if (title != null) {
            allBooks = bookService.getBookByTitle(title);
        } else if (author != null) {
            allBooks = bookService.getBookByAuthor(author);
        } else {
            allBooks = bookService.listAllBooks();
        }

        return allBooks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allBooks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book", description = "Get a book by it's id", responses = {
        @ApiResponse(description = "A book with the given id was found", responseCode = "200"),
        @ApiResponse(description = "A book with the given id was not found", responseCode = "404"),
        @ApiResponse(description = "An unexpected error occurred", responseCode = "500")
    })
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id){
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (BookNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    @Operation(summary = "Add a book", description = "Create a new book", responses = {
        @ApiResponse(description = "A new book was created", responseCode = "201")
    })
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book newBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update a book with given details", responses = {
        @ApiResponse(description = "The book was successfully updated", responseCode = "200"),
        @ApiResponse(description = "A book with the given id was not found", responseCode = "404"),
        @ApiResponse(description = "An unexpected error occurred", responseCode = "500")
    })
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book){
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (BookNotFoundException bex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Delete a book with the given id", responses = {
        @ApiResponse(description = "The book was successfully deleted", responseCode = "204"),
        @ApiResponse(description = "A book with the given id was not found", responseCode = "404"),
        @ApiResponse(description = "An unexpected error occurred", responseCode = "500")
    })
    public ResponseEntity<Book> deleteBook(@PathVariable("id") Long id){
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build(); // Best practice to return a No content on successful delete
        } catch (BookNotFoundException bex){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // some other unknown error occurred, possibly db, etc
        }
    }

}
