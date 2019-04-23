package top.systemsec.mycalendar.bean;

public class DateBean {

    private int mDay;//对应几号
    private boolean mLegal;//是否是法定节假日
    private String mLunarStr;//对应的阴历

    public DateBean(int day, boolean legal, String lunarStr) {
        mDay = day;
        mLegal = legal;
        mLunarStr = lunarStr;
    }

    public int getDay() {
        return mDay;
    }


    public boolean isLegal() {
        return mLegal;
    }

    public String getLunarStr() {
        return mLunarStr;
    }

}
