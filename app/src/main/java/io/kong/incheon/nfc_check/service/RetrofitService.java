package io.kong.incheon.nfc_check.service;

import java.util.List;

import io.kong.incheon.nfc_check.item.UserItem;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RetrofitService {
    @GET("user_table")
    Call<List<UserItem>> login(@Query("user_id") String user_id, @Query("user_pw") String user_pw);

}
