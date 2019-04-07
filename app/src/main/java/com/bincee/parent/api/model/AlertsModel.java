package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlertsModel {

//    public List<singleAlert> alerts = null;

//    public class singleAlert
//    {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public  List<EnclosingData> data = null;
    public class EnclosingData {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("last_updated")
        @Expose
        public String lastUpdated;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
    }
//    }
}
