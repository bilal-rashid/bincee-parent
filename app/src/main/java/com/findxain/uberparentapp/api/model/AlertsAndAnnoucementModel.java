package com.findxain.uberparentapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlertsAndAnnoucementModel {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public EnclosingData data;
    public class EnclosingData{
        @SerializedName("1")
        @Expose
        public List<AnnouncementModel> schoolAnnouncements = null;
        @SerializedName("2")
        @Expose
        public List<AlertModel> emergencyAlerts = null;
    }
    public class AlertModel{
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("last_updated")
        @Expose
        public String lastUpdated;
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
    public class AnnouncementModel
    {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("last_updated")
        @Expose
        public String lastUpdated;
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
}
