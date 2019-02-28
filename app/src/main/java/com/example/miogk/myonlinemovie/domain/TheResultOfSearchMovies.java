package com.example.miogk.myonlinemovie.domain;

import java.util.ArrayList;

public class TheResultOfSearchMovies {
    public ArrayList<Subjects> subjects;

    public class Subjects {
        public Rating rating;
        public ArrayList<String> genres;
        public String title;
        public String original_title;
        public String year;
        public Images images;
        public String id;
        public ArrayList<Casts> casts;
        public ArrayList<Directors> directors;


        public class Casts {
            public String name;
        }

        public class Directors {
            public String name;
        }

        public class Rating {
            public String max;
            public String average;
            public String stars;
            public String min;
        }

        public class Images {
            public String medium;
        }
    }
}