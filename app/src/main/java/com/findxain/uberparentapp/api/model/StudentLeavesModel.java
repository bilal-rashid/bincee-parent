package com.findxain.uberparentapp.api.model;

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
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("from_date")
        @Expose
        private String fromDate;
        @SerializedName("to_date")
        @Expose
        private String toDate;
        @SerializedName("student_id")
        @Expose
        private Integer studentId;
    }
}
