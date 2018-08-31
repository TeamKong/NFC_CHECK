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
    static final String TAG_URL = "http://13.209.207.99:3000";

    @GET("subject_table")
    Call<ResponseBody> subject_table(@Query("sbj_division") String sbj_division, @Query("sbj_category") String sbj_category, @Query("sbj_name") String sbj_name);

    @GET("user_table")
    Call<ResponseBody> login(@Query("user_id") String user_id, @Query("user_pw") String user_pw);

    @POST("join")
    Call<ResponseBody> join(@Query("user_id") String user_id, @Query("user_pw") String user_pw, @Query("user_university") String user_university,
                            @Query("user_name") String user_name, @Query("user_major") String user_major);

    @POST("person_subject")
    Call<ResponseBody> person_subject(@Query("user_id") String user_id,@Query("sbj_index") String sbj_index);

    @GET("person_subjectTable")
    Call<ResponseBody> person_subjectTable(@Query("user_id") String user_id);

    @GET("person_subject/delete")
    Call<ResponseBody> person_delete(@Query("user_id") String user_id, @Query("sbj_index") String sbj_index);

}
