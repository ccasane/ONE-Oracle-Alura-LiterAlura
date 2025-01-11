package com.one.literalura.repository;

import com.one.literalura.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameContainsIgnoreCase(String nameAuthor);

    @Query("SELECT a FROM Author a WHERE a.dateOfBirth <= :secondDate AND (a.dateOfDeath IS NULL OR a.dateOfDeath >= :firstDate) ORDER BY a.dateOfBirth")
    List<Author> findAliveAuthorAtDate(Integer firstDate, Integer secondDate);

    @Query("SELECT a.name, COUNT(l.title) FROM Author a JOIN Book l ON a.id = l.id GROUP BY a.name ORDER BY 2 DESC")
    List<Author> countBooksByAuthor();
}