package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetParentDataResponse {


    @Expose
    @SerializedName("kids")
    public List<KidsEntity> kids;
    @Expose
    @SerializedName("school")
    public SchoolEntity school;
    @Expose
    @SerializedName("school_id")
    public int schoolId;
    @Expose
    @SerializedName("photo")
    public String photo;
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

    public static class KidsEntity {
        @Expose
        @SerializedName("driver")
        public DriverEntity driver;
        @Expose
        @SerializedName("status")
        public String status;
        @Expose
        @SerializedName("driver_id")
        public int driverId;
        @Expose
        @SerializedName("parent_id")
        public int parentId;
        @Expose
        @SerializedName("shift")
        public ShiftEntity shift;
        @Expose
        @SerializedName("grade")
        public GradeEntity grade;
        @Expose
        @SerializedName("fullname")
        public String fullname;
        @Expose
        @SerializedName("id")
        public int id;
    }

    public static class DriverEntity {
        @Expose
        @SerializedName("status")
        public String status;
        @Expose
        @SerializedName("school_id")
        public int schoolId;
        @Expose
        @SerializedName("photo")
        public String photo;
        @Expose
        @SerializedName("phone_no")
        public String phoneNo;
        @Expose
        @SerializedName("fullname")
        public String fullname;
        @Expose
        @SerializedName("driver_id")
        public int driverId;
    }

    public static class ShiftEntity {
        @Expose
        @SerializedName("school_id")
        public int schoolId;
        @Expose
        @SerializedName("end_time")
        public String endTime;
        @Expose
        @SerializedName("start_time")
        public String startTime;
        @Expose
        @SerializedName("shift_name")
        public String shiftName;
        @Expose
        @SerializedName("shift_id")
        public int shiftId;
    }

    public static class GradeEntity {
        @Expose
        @SerializedName("school_id")
        public int schoolId;
        @Expose
        @SerializedName("grade_section")
        public String gradeSection;
        @Expose
        @SerializedName("section")
        public String section;
        @Expose
        @SerializedName("grade_name")
        public String gradeName;
        @Expose
        @SerializedName("grade_id")
        public int gradeId;
    }

    public static class SchoolEntity {
        @Expose
        @SerializedName("lng")
        public double lng;
        @Expose
        @SerializedName("lat")
        public double lat;
        @Expose
        @SerializedName("phone_no")
        public String phoneNo;
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("address")
        public String address;
        @Expose
        @SerializedName("school_id")
        public int schoolId;
    }
}
