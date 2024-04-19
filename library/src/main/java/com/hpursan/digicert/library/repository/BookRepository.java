package com.hpursan.digicert.library.repository;

import com.hpursan.digicert.library.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
