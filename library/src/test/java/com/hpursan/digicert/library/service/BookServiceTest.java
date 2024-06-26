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

import java.util.Arrays;
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
        Book book = new Book(1L, "ABC", "XYZ", "1234567890123");
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
        Book originalBook = new Book(1L, "The Shining", "Stephen King", "1234567890123");

        Book bookUpdates = new Book(1L, "IT", "Stephen King", "1234567890124");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(originalBook));
        when(bookRepository.save(any(Book.class))).thenReturn(originalBook);

        Book changedBook = bookService.updateBook(1L, bookUpdates);
        assertEquals("IT", changedBook.getTitle());

    }

    @Test
    void deleteBook_WhenBookExists_ShouldCallDeleteById(){
        Book book = new Book(1L, "ABC", "XYZ", "1234567890123");
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldThrowBookNotFoundException(){
        when(bookRepository.existsById(1L)).thenReturn(false);
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));

        assertTrue(ex.getMessage().contains("Book with id 1 not found"));
    }

    @Test
    void getBookByTitle_whenExists_ShouldReturnGivenBooks(){
        Book book1 = new Book(1L, "The Shining", "Stephen King", "1234567890123");
        Book book2 = new Book(2L, "Shining Girls", "Lauren Beukes", "1234567890246");
        when(bookRepository.findByTitleContainingIgnoreCase("Shining")).thenReturn(Arrays.asList(book1, book2));
        List<Book> books = bookService.getBookByTitle("Shining");
        assertEquals(2, books.size());
    }

    @Test
    void getBookByTitle_whenNoneExist_ShouldThrowBookNotFoundException(){
        when(bookRepository.findByTitleContainingIgnoreCase("Shining")).thenReturn(List.of());
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.getBookByTitle("Shining"));
        assertTrue(ex.getMessage().contains("No books with title containing Shining were found"));
    }

    @Test
    void getBookByAuthor_whenExists_ShouldReturnGivenBooks(){
        Book book1 = new Book(1L, "The Shining", "Stephen King", "1234567890123");
        Book book2 = new Book(2L, "Carrie", "Stephen King", "1234567890456");
        when(bookRepository.findByAuthorContainingIgnoreCase("Stephen King")).thenReturn(Arrays.asList(book1, book2));
        List<Book> books = bookService.getBookByAuthor("Stephen King");
        assertEquals(2, books.size());
    }

    @Test
    void getBookByAuthor_whenNoneExist_ShouldThrowBookNotFoundException(){
        when(bookRepository.findByAuthorContainingIgnoreCase("Stephen King")).thenReturn(List.of());
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.getBookByAuthor("Stephen King"));
        assertTrue(ex.getMessage().contains("No books with written by author names containing Stephen King were found"));
    }

}
