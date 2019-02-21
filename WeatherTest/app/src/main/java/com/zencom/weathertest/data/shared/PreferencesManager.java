package com.zencom.weathertest.data.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zencom.weathertest.pojo.Session;

public class PreferencesManager {

    private static String TAG = PreferencesManager.class.getSimpleName();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    private static String KEY = "couponsbussines";

    public static final String KEY_BASIC_AUTH = "key_basic_authentification";
    public static final String KEY_SESSION = "key_session";

    private static volatile PreferencesManager instance = null;

    private PreferencesManager(Context context) {
        this.context = context;
    }

    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PreferencesManager.class) {
                if (instance == null) {
                    instance = new PreferencesManager(context);
                }
            }
        }
        instance.init();
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public void saveSession(Session session) {
        initEditMode();
        Gson gson = new GsonBuilder().create();
        editor.putString(KEY_SESSION, gson.toJson(session));
        close();
    }

    public Session getSession() {
        if (!prefs.getString(KEY_SESSION, "").equals("")) {
            Gson gson = new GsonBuilder().create();
            String signupFlowJSON = prefs.getString(KEY_SESSION, "");
            return gson.fromJson(signupFlowJSON, Session.class);
        } else return null;
    }

    private void init() {
        prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    private void close() {
        editor.commit();
    }

    private void initEditMode() {
        editor = prefs.edit();
    }

    public void clean() {
        getSharedPreferences().edit().clear().apply();
    }


}