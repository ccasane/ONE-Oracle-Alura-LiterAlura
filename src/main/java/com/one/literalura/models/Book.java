package com.one.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    @Column(unique = true) private String title, nameAuthor, language;
    private Long nDownloads;

    @ManyToOne private Author author;

    public Book() {}

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.nDownloads = bookData.nDownloads();
        this.nameAuthor = bookData.author().getFirst().name();
        this.language = bookData.language().getFirst();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNameAuthor() { return nameAuthor; }
    public void setNameAuthor(String nameAuthor) { this.nameAuthor = nameAuthor; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Long getDownloads() { return nDownloads; }
    public void setDownloads(Long nDownloads) { this.nDownloads = nDownloads; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return String.format("Titulo = %s\nAutor = %s\nIdioma = %s\nNumero de descargas = %d\n----------------------------------------",
                title, nameAuthor, language, nDownloads);
    }
}