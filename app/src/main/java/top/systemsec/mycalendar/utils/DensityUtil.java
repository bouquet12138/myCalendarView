package top.systemsec.mycalendar.utils;


import top.systemsec.mycalendar.base.MyApplication;

/**
 * Created by xiaohan on 2017/11/18.
 */

public class DensityUtil {

    public static int dipToPx(float dpValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}