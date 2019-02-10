package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateLeaveResponce {


    @Expose
    @SerializedName("data")
    public DataEntity data;
    @Expose
    @SerializedName("status")
    public int status;

    public static class DataEntity {
        @Expose
        @SerializedName("school_id")
        public int schoolId;
        @Expose
        @SerializedName("comment")
        public String comment;
        @Expose
        @SerializedName("student_id")
        public int studentId;
        @Expose
        @SerializedName("to_date")
        public String toDate;
        @Expose
        @SerializedName("from_date")
        public String fromDate;
        @Expose
        @SerializedName("id")
        public int id;
    }
}
