package com.test.anjane.post.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class mPost {

    public String address;
    public String uid;
    public String author;
    public String title;
    public String body;
    public String image1;
    public String image2;
    public String image3;
    public String spin1;
    public String spin2;
    public String formatDate;
    public String date;

    public mPost() {  }

    public mPost(String address, String uid, String author, String title, String body, String image1, String image2, String image3, String spin1, String spin2, String formatDate, String date) {
        this.address = address;
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.spin1 = spin1;
        this.spin2 = spin2;
        this.formatDate = formatDate;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("image1", image1);
        result.put("image2", image2);
        result.put("image3", image3);
        result.put("spin1;", spin1);
        result.put("spin2;",spin2);
        result.put("formatDate", formatDate);
        result.put("date", date);

        return result;
    }
}
