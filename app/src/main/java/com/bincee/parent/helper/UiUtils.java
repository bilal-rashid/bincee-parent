package com.bincee.parent.helper;

import android.content.Context;
import android.util.DisplayMetrics;

public class UiUtils {
    public static int DptoPixel(Context context, int dp) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)
                ;
    }
}
