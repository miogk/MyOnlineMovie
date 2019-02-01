package com.example.miogk.myonlinemovie.domain;

import java.util.ArrayList;

public class AllReviews {
    public String count;
    public ArrayList<Review> reviews;

    public class Review {
        public Rating rating;
        public String useful_count;
        public Author author;
        public String created_at;
        public String title;
        public String updated_at;
        public String summary;
        public String content;
        public String useless_count;
        public String comments_count;


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
}
