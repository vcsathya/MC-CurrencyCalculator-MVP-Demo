package com.currencyconverter.vcsathya.mc.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    private static NetworkUtil networkUtil;

    public static NetworkUtil getNetworkUtil() {
        if (networkUtil == null) {
            networkUtil = new NetworkUtil();
        }
        return networkUtil;
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
