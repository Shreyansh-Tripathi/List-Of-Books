package com.example.booklist;

public class Book
{
    private String bookname;
    private String author;
    private String bookgenre;

    public Book(String bookname, String author, String bookgenre) {
        this.bookname = bookname;
        this.author = author;
        this.bookgenre = bookgenre;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookgenre() {
        return bookgenre;
    }

    public void setBookgenre(String bookgenre) {
        this.bookgenre = bookgenre;
    }
}
