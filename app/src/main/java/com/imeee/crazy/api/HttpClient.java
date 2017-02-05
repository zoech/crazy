package com.imeee.crazy.api;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.imeee.crazy.misc.FastJsonConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/1/25.
 */

public class HttpClient {
    private static Long TIMEOUT_LENGTH = 8L;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public HttpClient(){


        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(caikidInterceptor)
                .connectTimeout(TIMEOUT_LENGTH, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SecInfo.API_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    public ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }


    /**
     * Interceptor used to set those common header,
     * such as user account, id, etc.
     *
     * For more details, refer to the okhttp and retrofit2 docs.
     */
    private Interceptor caikidInterceptor= new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();

            builder.header("Referer","http://music.163.com");
            request = builder.build();


            /* debug used log information */
            Log.i("------------interceptor",request.headers().toString());
            Log.i("---------------------", request.toString());



            Response response = chain.proceed(request);

            return response;
        }
    };
}
