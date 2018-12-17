package com.findxain.uberparentapp.api;


import com.findxain.uberparentapp.api.model.AlertsAndAnnoucementModel;
import com.findxain.uberparentapp.api.model.GetParentDataResponse;
import com.findxain.uberparentapp.api.model.LoginResponse;
import com.findxain.uberparentapp.api.model.MyResponse;
import com.findxain.uberparentapp.api.model.ParentCompleteData;
import com.findxain.uberparentapp.api.model.ProfileResponse;
import com.findxain.uberparentapp.api.model.Student;
import com.findxain.uberparentapp.api.model.StudentLeavesModel;
import com.findxain.uberparentapp.api.model.UploadImageResponce;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("school/student/leaves/{studentId}")
    Observable<StudentLeavesModel> getStudentLeaves(@Path("studentId") String parentId);

    @Multipart
    @POST("avatar/upload")
    Observable<MyResponse<UploadImageResponce>> uploadImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("school/parent/{parentId}")
    Observable<MyResponse> updateProfile(@Path("parentId") String driverId, @Field("photo") String imageUrl);

    @GET("parent/getData/parentId")
    Observable<MyResponse<GetParentDataResponse>> getParentData(@Path("parentId") String paretnId);

    @GET("school/notification/parent/{parentId}")
    Observable<AlertsAndAnnoucementModel> getAlertsAndAnnouncements(@Path("parentId") String paretnId);

    @GET("parent/getData/{parentId}")
    Observable<MyResponse<ParentCompleteData>> getParentCompleteData(@Path("parentId") String parentId);

}
