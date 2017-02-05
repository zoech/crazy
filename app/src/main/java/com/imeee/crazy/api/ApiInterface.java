package com.imeee.crazy.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/1/25.
 */

public interface ApiInterface {


    @POST(SecInfo.RECORD_URL)
    Call<String> getRank(@Query(SecInfo.QUERY_CSRF_TOKEN) String csrf_token,
                            @Query(SecInfo.FIELD_PARAMS) String params,
                            @Query(SecInfo.FIELD_ENCSECKEY) String encSecKey);
}
