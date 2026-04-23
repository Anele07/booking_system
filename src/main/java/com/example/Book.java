package com.example;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;

    public Book(String title, String author, String isbn, int year){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = year;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getIsbn(){
        return isbn;
    }

    public int getYear(){
        return publishedYear;
    }

    public String toString(){
        return "Title: "+title+"\n"+
        "Author: "+author+"\n"+
        "ISBN: "+isbn+"\n"+
        "Publication Year: "+publishedYear;
    }

    public static void main(String[] args){
        Book book = new Book("1984", "George Orwell", "ISBN123", 1949);
        System.out.println(book);
    }
}