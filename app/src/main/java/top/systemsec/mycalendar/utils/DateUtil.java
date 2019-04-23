package top.systemsec.mycalendar.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String TAG = "DateUtil";

    /**
     * 返回某年某月有几天
     *
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(year + "-" + month + "-" + "01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return 0;//0天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 实现给定某日期，判断是星期几
     *
     * @return
     */
    public static int getWeekday(int year, int month) {//必须yyyy-MM-dd
        Calendar calendar = Calendar.getInstance();//获取一个实例
        calendar.set(year, month - 1, 1);//这个month 要减1
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;//周日 对应1 周一对应2
        Log.d(TAG, "getWeekday: week " + week);
        return week;
    }


}
