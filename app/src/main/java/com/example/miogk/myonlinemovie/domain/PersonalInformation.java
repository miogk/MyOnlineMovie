package com.example.miogk.myonlinemovie.domain;

import java.util.ArrayList;

public class PersonalInformation {
    //个人姓名
    public String name;
    //英语名字
    public String name_en;
    public ArrayList<Work> works;
    //职业
    public ArrayList<String> professions;

    //性别
    public String gender;

    //个人头像
    public Avatars avatars;
    //个人简介
    public String summary;
    //剧照
    public ArrayList<Work.Photo> photos;
    //生日
    public String birthday;
    //出生地
    public String born_place;
    //星座
    public String constellation;

    //个人图像
    public class Avatars {
        public String medium;
    }

    public class Work {
        //每部影片的定位
        public ArrayList<String> roles;
        public Subject subject;


        //剧照
        public class Photo {
            public String image;
        }

        public class Subject {
            public Rating rating;
            //电影类型-->科幻、灾难、喜剧等
            public ArrayList<String> genres;
            //电影名字
            public String title;
            public ArrayList<Casts> casts;
            //时间
            public ArrayList<String> durations;
            //导演
            public ArrayList<Director> directors;
            //上映时间
            public ArrayList<String> pubdates;
            //上映年份
            public String year;
            //图片
            public Image images;

            //影评ID
            public String id;


            public class Rating {
                public String max;
                public String average;
                public String stars;
            }

            //卡司
            public class Casts {
                public String name;
            }

            //导演
            public class Director {
                public String name;
            }

            //图片
            public class Image {
                //图片地址
                public String medium;
            }
        }
    }
}