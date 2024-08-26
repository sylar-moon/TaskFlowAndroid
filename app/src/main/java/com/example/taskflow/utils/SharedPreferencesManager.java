package com.example.taskflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "app_prefs";
    private static final String TOKEN_KEY = "auth_token";
   private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public void saveToken (String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY,token);
        editor.apply();
    }

    public String getToken (){
        return sharedPreferences.getString(TOKEN_KEY,null);
    }

}
