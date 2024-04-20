package com.hpursan.digicert.library.service;

import com.hpursan.digicert.library.domain.Book;
import com.hpursan.digicert.library.exception.BookNotFoundException;
import com.hpursan.digicert.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

public class BookServiceTest {
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    @Test
    void listAllBooks_ShouldReturnAllBooks(){
        when(bookRepository.findAll()).thenReturn(List.of(new Book(), new Book()));
        List<Book> books = bookService.listAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnGivenBook(){
        Book book = new Book(1L, "ABC", "XYZ", "1234-5678");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book book1 = bookService.getBookById(1L);
        assertEquals(book, book1);
    }

    @Test
    void getBookById_WhenNoBookExists_ShouldThrowBookNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));

        assertTrue(ex.getMessage().contains("Book with id 1 not found"));
    }

    @Test
    void addBook_ShouldReturnNewBook(){
        Book book = new Book();
        book.setTitle("The Shining");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book newBook = bookService.addBook(book);
        assertEquals(newBook.getTitle(), "The Shining");
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook(){
        Book originalBook = new Book(1L, "The Shining", "Stephen King", "1234-5678");

        Book bookUpdates = new Book(1L, "IT", "Stephen King", "1234-5678");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(originalBook));
        when(bookRepository.save(any(Book.class))).thenReturn(originalBook);

        Book changedBook = bookService.updateBook(1L, bookUpdates);
        assertEquals("IT", changedBook.getTitle());

    }

    @Test
    void deleteBook_ShouldCallDeleteById(){
        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }
}
