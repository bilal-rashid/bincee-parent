package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentLeavesModel {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public List<OneLeaveRecord> data = null;

    public class OneLeaveRecord {

        @Expose
        @SerializedName("school_id")
        public int schoolId;
        @Expose
        @SerializedName("student_id")
        public int studentId;

        public String comment;
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
