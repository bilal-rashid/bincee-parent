package com.bincee.parent.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementModel {

    //    public List<SingleAnnouncement> annoucements = null;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public List<SingleAnouncementTop> data = new ArrayList<>();

    public class SingleAnnouncement {
        public Integer id;
        public String lastUpdated;
        public String title;
        public String description;
        public String type;
        @SerializedName("school_id")
        @Expose
        public Integer schoolId;
    }

    public class SingleAnouncementTop {

        public int student_id;
        public List<SingleAnnouncement> data = new ArrayList<>();


    }


}

