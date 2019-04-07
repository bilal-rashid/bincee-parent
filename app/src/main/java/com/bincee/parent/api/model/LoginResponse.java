package com.bincee.parent.api.model;

import com.bincee.parent.MyApp;


public class LoginResponse {


    public User data;
    public int status;

    public static class User {
        public String token;
        public int type;
        public String username;
        public int id;
        public String message;
        public ParentCompleteData parentCompleteInfo = new ParentCompleteData();


        @Override
        public String toString() {
            return MyApp.instance.gson.toJson(this);
        }
    }
}
