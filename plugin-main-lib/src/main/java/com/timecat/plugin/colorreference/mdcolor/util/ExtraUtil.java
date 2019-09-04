package com.timecat.plugin.colorreference.mdcolor.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class ExtraUtil {
    /**
     * Go to app market to view details of this app.
     *
     * @param viaBrowser Whether view via browser or client app.
     */
    public static void gotoMarket(Context context, boolean viaBrowser) {
        String packageName = context.getPackageName();

        final String LINK = String.format((viaBrowser
                ? "https://play.google.com/store/apps/details?id=%s"
                : "market://details?id=%s"), packageName);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(LINK));

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            if (!viaBrowser) {
                gotoMarket(context, true);
            }
        }
    }
    public static int getVersionCode(Context context) {
        if (context == null) {
            return 0;
        } else {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException var2) {
                var2.printStackTrace();
                return 0;
            }
        }
    }

}
