package com.example.to_do_list.DashData;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    public String username;
    public String email;

    public final static String PREF_NAME = "ToDoList";
    public final static String PREF_DEF = "";

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    public static void savePrefs(Context ctx, String key, String value) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadPrefs(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, PREF_DEF);
    }

    public static void savePrefs(Context ctx, String key, boolean value) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean loadPrefs(Context ctx, String key, boolean type) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }
}
