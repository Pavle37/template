package com.creitive.templateapplication.network;

import com.creitive.templateapplication.model.Token;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Used to map GET and POST requests to their pojo classes
 */
public interface APIService {

    @Headers("Authorization: Token 9944b09199c62bcf9418ad846dd0e4bbdfc6ee4b")
    @POST("post")
    Observable<Token> registerUser(@Path("isRegistering") String isRegistering, @Body String body);

}
