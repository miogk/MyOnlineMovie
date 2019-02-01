package com.example.miogk.myonlinemovie.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * count	int	单页条数
 * start	int	数据的开始项
 * total	int	数据总条数
 * subjects	json数组	电影列表
 * — id	string	电影id
 * — title	string	电影名中文名
 * — images	json对象	存放各种大小的电影图
 * —— small／large／medium	string	电影图url
 * — genres	json数组	电影类型
 * — rating	json对象	评分信息
 * —— average	float	电影评分
 * — directors	json数组	导演列表
 * —— name	string	导演名
 * — casts	json数组	主演列表
 * —— name	string	主演名
 * — year	int	年份
 */


/**
 * https://douban.uieee.com/v2/movie/in_theaters
 */


public class HotMovie implements Serializable {
    public String count;
    public String start;
    public String total;
    public String title;
    public ArrayList<Subjects> subjects;

    public class Subjects implements Serializable {
        public Rating rating;
        public ArrayList<String> genres;
        public String title;
        public ArrayList<Casts> casts;
        public ArrayList<String> durations;
        public String collect_count;
        public String mainland_pubdate;
        public String original_title;
        public String subtype;
        public ArrayList<String> pubdates;
        public String year;
        public String alt;
        public String id;
        public ArrayList<Directors> directors;
        public Images images;

        public class Rating implements Serializable {
            String max;
            public String average;
            public String stars;
            String min;
        }

        public class Casts implements Serializable {
            public String alt;
            public String name;
            public String name_en;
            public String id;
            public Avatars avatars;

            public class Avatars {
                public String small;
                public String large;
                public String medium;
            }
        }

        public class Directors implements Serializable {
            public String alt;
            public String name;
            public String name_en;
            public String id;
            public Casts.Avatars avatars;

            public class Avatars {
                public String small;
                public String large;
                public String medium;
            }
        }

        public class Images implements Serializable {
            public String small;
            public String large;
            public String medium;
        }
    }
}