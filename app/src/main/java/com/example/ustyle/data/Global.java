package com.example.ustyle.data;

import android.content.Context;
import android.util.Log;

public class Global {

    public static String accessToken = "";
    public static String env = "hkustlife-1gmkzslzdce7a450";
    public static Context context = null;
    public static int threadCount = 0;

    public static class User {
        public static Boolean checked = false;
        public static Boolean loggedIn = false;
        public static String userID = "";
        public static String androidID = "";
        public static String userName = "";
        public static String itsc = "";
    }

    public static class BaseURL {
        public static String queryBaseURL = "https://api.weixin.qq.com/tcb/databasequery?";
        public static String addBaseURL = "https://api.weixin.qq.com/tcb/databaseadd?";
        public static String cloudFunctionURL = "https://api.weixin.qq.com/tcb/invokecloudfunction?";
    }

    public static void retrieveAccessToken() {

        new AccessToken().execute();
    }
}
