package com.example.miogk.myonlinemovie.domain;

public class SingleReview {
    public AllReviews.Review.Rating rating;
    public String useful_count;
    public AllReviews.Review.Author author;
    public String created_at;
    public String title;
    public String updated_at;
    public String summary;
    public String content;
    public String useless_count;
    public String comments_count;
    public String id;


    public class Author {
        public String avatar;
        public String name;
    }

    public class Rating {
        public String max;
        public String value;
        public String min;
    }
}
