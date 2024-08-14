package jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookdetails")
public class BookDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "price")
    private int price;

    @Column(name = "numberOfPage")
    private int numberOfPage;

    @Column(name = "publish_date")
    private LocalDate publishDate;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    // Constructors, Getters and Setters

    public BookDetailsEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(int numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
        if (book.getBookDetails() != this) {
            book.setBookDetails(this);
        }
    }

    @Override
    public String toString() {
        return "BookDetailsEntity{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", numberOfPage=" + numberOfPage +
                ", publishDate=" + publishDate +
                '}';
    }
}