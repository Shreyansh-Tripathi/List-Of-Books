package com.example.booklist;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Book> list;

    @Override
    public void onCreate() {
        super.onCreate();

        list=new ArrayList<Book>();
        list.add(new Book("The Fault In Our Stars", "John Green", "love"));
        list.add(new Book("Looing For Alaska", "John Green", "love"));
        list.add(new Book("Frankenstein", "Mary Shelley", "sci fi"));
        list.add(new Book("The Stars My Destination", "Alfred Bester", "sci fi"));
        list.add(new Book("American Psycho", "Bret Easton Ellis", "horror"));
        list.add(new Book("At the Mountains of Madness", "H.P. Lovecraft", "horror"));
        list.add(new Book("Harry Potter", "JK Rowling", "horror"));
    }
}
