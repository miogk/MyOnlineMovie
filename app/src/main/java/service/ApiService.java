package service;

import com.example.miogk.myonlinemovie.domain.AllComments;
import com.example.miogk.myonlinemovie.domain.AllReviews;
import com.example.miogk.myonlinemovie.domain.HotMovie;
import com.example.miogk.myonlinemovie.domain.HotMovieContent;
import com.example.miogk.myonlinemovie.domain.PersonalInformation;
import com.example.miogk.myonlinemovie.domain.ShowPhotos;
import com.example.miogk.myonlinemovie.domain.SingleReview;
import com.example.miogk.myonlinemovie.domain.TheResultOfSearchMovies;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    /**
     * 获取正在热映的电影：
     * <p>
     * 接口：https://api.douban.com/v2/movie/in_theaters
     */
    @GET("/")
    Observable<HotMovie> getData();

    /**
     * 获取正在热映的电影：
     * https://douban.uieee.com/v2/movie/in_theaters
     */
    @POST("v2/movie/in_theaters")
    Call<HotMovie> getHotMovie();

    @GET("v2/movie/in_theaters")
    Observable<HotMovie> getHotMovieUserRxjava(@Query("city") String city, @Query("start") int start, @Query("count") int count);

    @GET("v2/movie/coming_soon")
    Observable<HotMovie> getComingMovieUserRxjava(@Query("city") String city, @Query("start") int start, @Query("count") int count);

    @GET("v2/movie/subject/{movieId}")
    Observable<HotMovieContent> getHotMovieContentWithRxJava(@Path("movieId") String movieId);

    @GET("v2/movie/subject/{movieId}/photos")
    Observable<ShowPhotos> getPhotosWitRxJava(@Path("movieId") String movieId, @Query("start") String start, @Query("count") String count);

    @GET("v2/movie/subject/{movieId}/comments")
    Observable<AllComments> getAllComments(@Path("movieId") String movieId, @Query("start") String start, @Query("count") String count);

    @GET("v2/movie/subject/{movieId}/reviews")
    Observable<AllReviews> getAllReviews(@Path("movieId") String movieId, @Query("start") String start, @Query("count") String count);

    @GET("v2/movie/review/{reviewId}")
    Observable<SingleReview> getSingleReview(@Path("reviewId") String reviewId);

    @GET("v2/movie/search")
    Observable<TheResultOfSearchMovies> getTheResultOfSearchMovies(@Query("q") String q);

    @GET("v2/movie/celebrity/{id}")
    Observable<PersonalInformation> getPeronalInformation(@Path("id") String id);
}
