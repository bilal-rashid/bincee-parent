package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentCompleteData {
    @SerializedName("parent_id")
    @Expose
    public Integer parentId;
    @SerializedName("fullname")
    @Expose
    public String fullname;
    @SerializedName("phone_no")
    @Expose
    public String phoneNo;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("school_id")
    @Expose
    public Integer schoolId;
    @SerializedName("school")
    @Expose
    public SchoolModel school;
    @SerializedName("kids")
    @Expose
    public List<KidModel> kids = null;

    public static class DriverModel{
        @SerializedName("driver_id")
        @Expose
        public Integer driverId;
        @SerializedName("fullname")
        @Expose
        public String fullname;
        @SerializedName("phone_no")
        @Expose
        public String phoneNo;
        @SerializedName("photo")
        @Expose
        public String photo;
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
        @SerializedName("status")
        @Expose
        public String status;
    }
    public  class GradeModel{
        @SerializedName("grade_id")
        @Expose
        public Integer gradeId;
        @SerializedName("grade_name")
        @Expose
        public String gradeName;
        @SerializedName("section")
        @Expose
        public String section;
        @SerializedName("grade_section")
        @Expose
        public String gradeSection;
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
    }
    public  class KidModel{
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("fullname")
        @Expose
        public String fullname;
        @SerializedName("grade")
        @Expose
        public GradeModel grade;
        @SerializedName("photo")
        @Expose
        public Object photo;
        @SerializedName("shift")
        @Expose
        public ShiftModel shift;
        @SerializedName("parent_id")
        @Expose
        public Integer parentId;
        @SerializedName("driver_id")
        @Expose
        public Integer driverId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("driver")
        @Expose
        public DriverModel driver;

    }
    public   class SchoolModel{
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("phone_no")
        @Expose
        public String phoneNo;
        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;
    }

    public  class ShiftModel
    {
        @SerializedName("shift_id")
        @Expose
        public Integer shiftId;
        @SerializedName("shift_name")
        @Expose
        public String shiftName;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("end_time")
        @Expose
        public String endTime;
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
    }
}
