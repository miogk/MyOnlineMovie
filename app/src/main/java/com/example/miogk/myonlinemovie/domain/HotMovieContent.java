package com.example.miogk.myonlinemovie.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class HotMovieContent implements Serializable {
    public Rating rating;
    public String original_title;
    public String year;
    public String title;
    public String wish_count;//想看人数
    public String ratings_count;//评分人数
    public String summary;//简介
    public String photos_count;//总照片数
    public String comments_count;//总短评数
    public String reviews_count;//总影评数
    ArrayList<String> aka; //又名
    public Images images;
    public ArrayList<Photos> photos;
    public ArrayList<String> genres;
    public ArrayList<String> countries;
    public ArrayList<String> durations;
    public ArrayList<String> languages;
    public ArrayList<String> pubdates;
    public ArrayList<Casts> directors;
    public ArrayList<Casts> casts;
    public ArrayList<Trailers> trailers;//预告片
    public ArrayList<Bloopers> bloopers;//花絮
    public ArrayList<Clips> clips;//片段
    public ArrayList<String> tags;//标签
    public ArrayList<Comments> popular_comments;
    public ArrayList<Reviews> popular_reviews;


    public class Reviews {
        //影评标题
        public String title;
        public String name;
        public Rating rating;
        public Author author;
        //简介
        public String summary;

        //评分
        public class Rating {
            public String max;
            public String value;
            public String min;
        }

        //评分用户
        public class Author {
            //头像
            public String avatar;
            //姓名
            public String name;
        }
    }


    //短评
    public class Comments {
        public Rating rating;
        public Author author;
        //评论
        public String content;
        //觉得评分是有用的人数
        public String useful_count;
        //写下评论的时间
        public String created_at;

        //评分
        public class Rating {
            public String max;
            public String value;
            public String min;
        }

        //评分用户
        public class Author {
            //头像
            public String avatar;
            //姓名
            public String name;
        }
    }

    //片段
    public class Clips {
        public String medium;
        public String title;
        public String resource_url;
    }


    //花絮
    public class Bloopers {
        public String medium;
        public String title;
        public String resource_url;
    }

    //预告片
    public class Trailers {
        public String medium;
        public String title;
        public String resource_url;
    }


    //演职人员表
    public class Casts implements Serializable {
        public Avatar avatars;
        public String name_en;
        public String name;
        public String id;

        public class Avatar implements Serializable {
            public String small;
        }
    }


    public class Images {
        String small;
        public String large;
        String medium;
    }


    //评分
    public class Rating {
        public String max;
        public String average;
        public String stars;
        public HashMap<String, Float> details;
    }

    public class Photos {
        public String image;
        public String thumb;
    }
}