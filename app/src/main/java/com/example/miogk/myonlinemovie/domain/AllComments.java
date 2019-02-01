package com.example.miogk.myonlinemovie.domain;

import java.util.ArrayList;

public class AllComments {
    //短评论
    public ArrayList<Comments> comments;

    //短评
    public class Comments {
        public HotMovieContent.Comments.Rating rating;
        public HotMovieContent.Comments.Author author;
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
}
