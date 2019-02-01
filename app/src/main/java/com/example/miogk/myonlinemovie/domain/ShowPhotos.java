package com.example.miogk.myonlinemovie.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowPhotos implements Serializable {
    public String count;
    public ArrayList<Photo> photos;

    public class Photo {
        public String thumb;
    }
}
