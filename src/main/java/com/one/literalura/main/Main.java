package com.one.literalura.main;

import com.one.literalura.models.*;
import com.one.literalura.repository.AuthorRepository;
import com.one.literalura.repository.BookRepository;
import com.one.literalura.service.ApiClient;
import com.one.literalura.service.DataConvert;

import java.util.*;

public class Main {
    private final Scanner keyboard = new Scanner(System.in);
    private final ApiClient apiClient = new ApiClient();
    private final DataConvert dataConvert = new DataConvert();

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private List<Book> books;
    private List<Author> authors;

    public Main(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void showMenu(){
        int option;
        do {
            printMenu();
            option = getInputOption();
            handleMenuOption(option);
        } while (option != 0);
    }

    private void printMenu() {
        System.out.println("""
            =============================================================
            =                !Bienvenidos a LiterAlura!                 =
            =============================================================
            ==   1.- Agregar titulo del libro                          ==
            ==   2.- Listado de libros buscados                        ==
            ==   3.- Listado de Autores                                ==
            ==   4.- Listado de Autores vivos en determinado año       ==
            ==   5.- Listado de Libros por idioma                      ==
            ==   0.- Salir                                             ==
            =============================================================
            ==           Ingrese la opción que desea realizar:         ==
            =============================================================
            """);
    }

    private int getInputOption() {
        try {
            return Integer.parseInt(keyboard.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("""
                    =============================================================
                    ==               ¡Ingrese una opción valida!               ==
                    =============================================================
                    """);
            return -1;
        }
    }

    private void handleMenuOption(int option) {
        switch (option) {
            case 1: searchBook(); break;
            case 2: bookFind(); break;
            case 3: listAuthors(); break;
            case 4: listAuthorsLive(); break;
            case 5: listBooksByLanguage(); break;
            case 0: System.out.println("""
                    =============================================================
                    ==                 ¡Cerrando Aplicación!                   ==
                    =============================================================
                    """); break;
            default: System.out.println("""
                    =============================================================
                    ==          Opción Incorrecta. ¡Vuelva a intentarlo!       ==
                    =============================================================
                    """); break;
        }
    }

    private void searchBook(){
        Data data = getDataBook();
        Optional<BookData> bookData = data.results().stream().findFirst();
        bookData.ifPresentOrElse(this::processBookData, () -> System.out.println("\n***** ¡Libro no encontrado! *****\n"));
    }

    private Data getDataBook() {
        System.out.println("""
                    =============================================================
                    ==       Ingrese el nombre del libro que desea buscar:     ==
                    =============================================================
                    """);
        String nameBook = keyboard.nextLine().toLowerCase().replace(" ", "%20");
        String URL_BASE = "https://gutendex.com/books/?search=";
        String json = apiClient.getData(URL_BASE + nameBook);
        return dataConvert.dataConvert(json, Data.class);
    }

    private void processBookData(BookData bookData) {
        Book book = new Book(bookData);
        Author author = new Author(bookData.author().getFirst());

        if (bookRepository.existsByTitle(book.getTitle())) {
            System.out.println("""
                =============================================================
                ==          ¡Este libro ya se encuentra registrado!        ==
                =============================================================
                """);
        } else {
            Optional<Author> existingAuthor = authorRepository.findByNameContainsIgnoreCase(author.getName());
            if (existingAuthor.isPresent()) {
                book.setAuthor(existingAuthor.get());
            } else {
                author.setAuthorBook(book);
                authorRepository.save(author);
            }
            bookRepository.save(book);
            System.out.println(book.toString());
            System.out.println("""
                =============================================================
                ==              ¡Libro registrado con éxito!               ==
                =============================================================
                """);
        }
    }

    private void bookFind() {
        System.out.println("""
                =============================================================
                ==              Los libros registrados son:                ==
                =============================================================
                """);
        bookRepository.findAll().forEach(System.out::println);
    }

    private void listAuthors() {
        System.out.println("""
                =============================================================
                ==                   Listado de Autores:                   ==
                =============================================================
                """);
        authorRepository.findAll().forEach(System.out::println);
    }

    private void listAuthorsLive(){
        int referenceYear;

        while (true) {
            System.out.println("""
                =============================================================
                ==              Indique el año de referencia:              ==
                =============================================================
                """);

            if (keyboard.hasNextInt()) {
                referenceYear = keyboard.nextInt();
                keyboard.nextLine();

                break;
            } else {
                System.out.println("""
                =============================================================
                ==                  ¡Opción no valida!                     ==
                =============================================================
                """);
                keyboard.nextLine();
            }
        }

        authorRepository.findAll().stream()
                .filter(author -> author.getDateOfBirth() != null && author.getDateOfDeath() != null
                        && referenceYear <= author.getDateOfDeath() && referenceYear >= author.getDateOfBirth())
                .forEach(System.out::println);
    }

    private void listBooksByLanguage() {
                System.out.println("""
                =============================================================
                ==   Indique el idioma de los libros a listar:             ==
                ==                                                         ==
                ==   1.- Español   (es)                                    ==
                ==   2.- Inglés    (en)                                    ==
                ==   3.- Frances   (fr)                                    ==
                ==   4.- Portugués (pt)                                    ==
                ==   5.- Italiano  (it)                                    ==
                =============================================================
                """);

        Map<String, String> languageOptions = Map.of(
                "1", "es",
                "2", "en",
                "3", "fr",
                "4", "pt",
                "5", "it"
        );

        String option = keyboard.nextLine().trim();
        String language = languageOptions.get(option);

        if (language != null) {
            System.out.printf("""
                =============================================================
                ==                    Libros en %s:                        ==
                =============================================================
                """, language);
            bookRepository.findAll().stream()
                    .filter(book -> book.getLanguage().equals(language))
                    .forEach(System.out::println);
        } else {
            System.out.println("""
                =============================================================
                ==                    ¡Opción no valida!                   ==
                =============================================================
                """);
        }
    }
}