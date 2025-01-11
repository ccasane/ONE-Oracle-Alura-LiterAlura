package com.one.literalura.repository;

import com.one.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);
}