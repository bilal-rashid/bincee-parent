package com.bincee.parent.api;


import com.bincee.parent.api.model.AlertsModel;
import com.bincee.parent.api.model.AnnouncementModel;
import com.bincee.parent.api.model.CreateLeaveResponce;
import com.bincee.parent.api.model.GetParentDataResponse;
import com.bincee.parent.api.model.LoginResponse;
import com.bincee.parent.api.model.MyResponse;
import com.bincee.parent.api.model.ParentCompleteData;
import com.bincee.parent.api.model.ProfileResponse;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.api.model.StudentLeavesModel;
import com.bincee.parent.api.model.UploadImageResponce;

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

    String BaseUrl = "http://access.bincee.com/";

    @FormUrlEncoded
    @POST("auth/login")
    Observable<LoginResponse> login(@Field("username") String username, @Field("password") String pass);


    @GET("school/parent/student/{parentId}")
    Observable<MyResponse<ArrayList<Student>>> parentsStudentsList(@Path("parentId") String parentId);


    @GET("school/parent/{parentId}")
    Observable<MyResponse<ProfileResponse>> getParent(@Path("parentId") String parentId);

    @GET("school/student/leaves/{studentId}")
    Observable<StudentLeavesModel> getStudentLeaves(@Path("studentId") String parentId);


    @POST("school/leaves/create")
    Observable<CreateLeaveResponce> createLeave(@Field("from_date") String from_date
            , @Field("to_date") String to_date
            , @Field("student_id") String student_id
            , @Field("comment") String comment
    );

    @Multipart
    @POST("avatar/upload")
    Observable<MyResponse<UploadImageResponce>> uploadImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("school/parent/{parentId}")
    Observable<MyResponse> updateProfile(@Path("parentId") String driverId, @Field("photo") String imageUrl);

    @GET("parent/getData/parentId")
    Observable<MyResponse<GetParentDataResponse>> getParentData(@Path("parentId") String paretnId);


    @GET("school/notification/parent/{parentId}")
    Observable<AnnouncementModel> getAnnouncements(@Path("parentId") String parentId);

    @GET("school/notification/list/{schoolId}")
    Observable<AlertsModel> getAlerts(@Path("schoolId") String schoolId);

    @GET("parent/getData/{parentId}")
    Observable<MyResponse<ParentCompleteData>> getParentCompleteData(@Path("parentId") String parentId);

}
