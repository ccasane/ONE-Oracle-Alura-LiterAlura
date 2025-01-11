package com.one.literalura.models;

import jakarta.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(unique = true) private String name;
    private Integer dateOfBirth, dateOfDeath;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> booksAuthor = new ArrayList<>();

    public Author() {}

    public Author(AuthorData dataAuthor) {
        this.name = dataAuthor.name();
        this.dateOfBirth = dataAuthor.dateOfBirth();
        this.dateOfDeath = dataAuthor.dateOfDeath();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Integer dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Integer getDateOfDeath() { return dateOfDeath; }
    public void setDateOfDeath(Integer dateOfDeath) { this.dateOfDeath = dateOfDeath; }

    public List<String> getBooksAuthor() {
        return booksAuthor.stream().map(Book::getTitle).collect(Collectors.toList());
    }

    public void setAuthorBook(Book book) {
        booksAuthor.add(book);
        book.setAuthor(this);
    }

    @Override
    public String toString(){
        return String.format("Autor = %s\nFecha de Nacimiento: %d\nFecha de Fallecimiento: %d\nLibros: %s\n-------------------------------------------",
                name, dateOfBirth, dateOfDeath, getBooksAuthor());
    }
}