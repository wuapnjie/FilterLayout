package com.xiaopo.flying.filterlayout.util;

import android.content.Context;

/**
 * Created by snowbean on 16-7-6.
 */
public class DeviceUtil {
    private static int sScreenWidth = 0;

    public static int getScreenWidth(Context context) {
        if (sScreenWidth == 0) {
            sScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return sScreenWidth;
    }
}
