package com.jmrandombitz.librarygenesis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamal on 22/03/2015.
 */
public class Book {
    public String title;
    public String dlLink;

    public Book(String title, String dlLink) {
        this.title = title;
        this.dlLink = dlLink;
    }

    public static ArrayList<Book> getBookInfo(ArrayList bookInfoSent) {
        ArrayList<Book> bookInfo = new ArrayList<>();
        for (int i=0;i<bookInfoSent.size();i++) {
            bookInfo.add(new Book(((List<String>)bookInfoSent.get(i)).get(0).toString(), ((List<String>)bookInfoSent.get(i)).get(1).toString()));
        }
        return bookInfo;
    }
}
