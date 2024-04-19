package com.hpursan.digicert.library.service;

import com.hpursan.digicert.library.domain.Book;
import com.hpursan.digicert.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> listAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) throws Exception {
        return bookRepository.findById(id).orElseThrow(() -> new Exception("Book with id " + id + " not found"));
    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) throws Exception {
        try {
            Book book = getBookById(id);
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setTitle(updatedBook.getTitle());
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new Exception("Book with id " + id + " not found");
        }
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}
