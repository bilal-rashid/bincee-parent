package com.bincee.parent.api.model;

import android.content.Context;
import android.content.SharedPreferences;

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

        public void save(Context context) {
            SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            pref.edit().putString(User.class.getSimpleName(), toString()).apply();

        }

        @Override
        public String toString() {
            return MyApp.instance.gson.toJson(this);
        }
    }
}
