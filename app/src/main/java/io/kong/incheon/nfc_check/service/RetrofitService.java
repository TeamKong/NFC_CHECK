package io.kong.incheon.nfc_check.service;

import java.util.List;

import io.kong.incheon.nfc_check.item.UserItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("{table}")
    Call<ResponseBody> subject_table(@Path("table") String table);

    @GET("user_table")
    Call<List<UserItem>> login(@Query("user_id") String user_id, @Query("user_pw") String user_pw);

    @POST("join")
    Call<ResponseBody> join(@Query("user_id") String user_id, @Query("user_pw") String user_pw, @Query("user_university") String user_university,
                            @Query("user_name") String user_name, @Query("user_major") String user_major, @Query("user_grade") String user_grade);
}
