package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnnouncementModel {

//    public List<SingleAnnouncement> annoucements = null;

//    public class SingleAnnouncement
//    {
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
//    }
}

