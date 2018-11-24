package com.findxain.uberparentapp.api;


import com.findxain.uberparentapp.api.model.LoginResponse;
import com.findxain.uberparentapp.api.model.MyResponse;
import com.findxain.uberparentapp.api.model.ProfileResponse;
import com.findxain.uberparentapp.api.model.Student;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {

    String BaseUrl = "https://bincee-server.herokuapp.com/api/";

    @FormUrlEncoded
    @POST("auth/login")
    Observable<LoginResponse> login(@Field("username") String username, @Field("password") String pass);


    @GET("school/parent/student/{parentId}")
    Observable<MyResponse<ArrayList<Student>>> parentsStudentsList(@Path("parentId") String parentId);


    @GET("school/parent/{parentId}")
    Observable<MyResponse<ProfileResponse>> getParent(@Path("parentId") String parentId);

    @GET("school/student/leaves/{student}")
    Observable<MyResponse<ProfileResponse>> getStudentLeaves(@Path("parentId") String parentId);




}
