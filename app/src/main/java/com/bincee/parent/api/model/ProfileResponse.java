package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @Expose
    @SerializedName("school_id")
    public int schoolId;
    @Expose
    @SerializedName("status")
    public String status;
    @Expose
    @SerializedName("lng")
    public double lng;
    @Expose
    @SerializedName("lat")
    public double lat;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("address")
    public String address;
    @Expose
    @SerializedName("phone_no")
    public String phoneNo;
    @Expose
    @SerializedName("fullname")
    public String fullname;
    @Expose
    @SerializedName("parent_id")
    public int parentId;

    public String photo;

    public String message;
}
