package com.hpursan.digicert.library.service;

import com.hpursan.digicert.library.domain.Book;
import com.hpursan.digicert.library.exception.BookNotFoundException;
import com.hpursan.digicert.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> listAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
    }

    public List<Book> getBookByTitle(String title) throws BookNotFoundException {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);

        if (books.isEmpty()) {
            throw new BookNotFoundException("No books with title containing " + title + " were found");
        }

        return books;

    }

    public List<Book> getBookByAuthor(String author) throws BookNotFoundException {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books with written by author names containing " + author + " were found");
        }

        return books;

    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book book = getBookById(id);
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setTitle(updatedBook.getTitle());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

}
