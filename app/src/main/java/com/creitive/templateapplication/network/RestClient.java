package com.creitive.templateapplication.network;


import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that does connecting to the API
 */

public class RestClient {

    private static RestClient mInstance = null;
    private static final String BASE_URL = "com.example.api";

    public APIService service;

    private static Retrofit sRetrofit;

    public static RestClient getInstance() {
        if(mInstance == null){
            mInstance = new RestClient();
        }
        return mInstance;
    }

    private RestClient(){
        //Used for logging of requests to and responses from server
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        GsonConverterFactory factory = GsonConverterFactory.create(
                new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
        );

        sRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .build();

        service = sRetrofit.create(APIService.class);
    }
}
