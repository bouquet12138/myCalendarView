package top.systemsec.mycalendar.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.systemsec.mycalendar.bean.DateBean;
import top.systemsec.mycalendar.bean.MyCalendarProperty;
import top.systemsec.mycalendar.utils.DateUtil;
import top.systemsec.mycalendar.utils.DensityUtil;
import top.systemsec.mycalendar.utils.SolarLunarConverter;


public class MyCalendar extends View {

    private static final String TAG = "MyCalendar";

    private String[] mWeekTitles = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    private DateBean mDateBean[][] = new DateBean[6][7];//日期bean

    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);//文字画笔
    private TextPaint mLunarTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);//文字画笔
    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//画笔
    private Paint mBackPaint = new Paint();//背景画笔

    private int mYear, mMonth;//当前年月
    private int mSpaceX, mSpaceY;//间隔X 间隔Y

    private MyCalendarProperty mProperty;

    public MyCalendar(Context context) {
        super(context);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化View
     */
    private void initPaint() {

        if (mProperty == null)
            return;

        mTextPaint.setTextAlign(Paint.Align.CENTER);//居中

        mLunarTextPaint.setColor(mProperty.getLunarColor());//阴历颜色
        mLunarTextPaint.setTextSize(mProperty.getLunarSize());//阴历大小
        mLunarTextPaint.setTextAlign(Paint.Align.CENTER);//居中

        mCirclePaint.setColor(mProperty.getTodayCircleColor());//蓝色

        mBackPaint.setColor(mProperty.getSelectedBackColor());//选中的颜色
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        initDate();
        drawTitle(canvas);
        drawDate(canvas);
    }

    /**
     * 初始化数据
     */
    private void initDate() {

        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++)
                mDateBean[i][j] = null;//置空
        int nowRow = 0;//当前行 当前列
        int nowColumn = DateUtil.getWeekday(mYear, mMonth);//得到当前是周几

        int daysOfMonth = DateUtil.getDaysOfMonth(mYear, mMonth);//这个月有几天
        for (int i = 1; i <= daysOfMonth; i++) {

            DateBean dateBean = SolarLunarConverter.SolarToLunar(mYear, mMonth, i);
            mDateBean[nowRow][nowColumn] = dateBean;
            nowColumn++;
            if (nowColumn > 6) {
                nowColumn = 0;
                nowRow++;//当前行加加
            }
        }

    }

    /**
     * 绘制标题栏
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        //分割宽度
        int left = mSpaceX / 2;
        int centerY = mSpaceY / 2;

        mTextPaint.setTextSize(mProperty.getTitleSize());//标题尺寸
        mTextPaint.setColor(mProperty.getTitleColor());//标题颜色

        for (int i = 0; i < mWeekTitles.length; i++) {
            canvas.drawText(mWeekTitles[i], left, centerY, mTextPaint);
            left += mSpaceX;
        }
    }

    /**
     * 绘制日期
     */
    private void drawDate(Canvas canvas) {

        int left = mSpaceX / 2;
        int topY = mSpaceY / 2 * 3;
        mTextPaint.setTextSize(mProperty.getDaySize());//上面数字的尺寸

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (mDateBean[i][j] != null) {//不为空
                    int centerX = j * mSpaceX + left;
                    int centerY = i * mSpaceY + topY;


                    if (MyCalendarView.isSelected(mYear, mMonth, mDateBean[i][j].getDay())) {
                        mTextPaint.setColor(mProperty.getSelectedTextColor());
                        int space = Math.min(mSpaceX, mSpaceY) / 2;
                        int textWidth = textWidth(mLunarTextPaint, mDateBean[i][j].getLunarStr());//文本宽度
                        Rect rect;
                        if (textWidth > 2 * space)
                            rect = new Rect(centerX - textWidth / 2, centerY - mSpaceY / 2, centerX + textWidth / 2, centerY + mSpaceY / 2);
                        else
                            rect = new Rect(centerX - space, centerY - mSpaceY / 2, centerX + space, centerY + mSpaceY / 2);
                        canvas.drawRect(rect, mBackPaint);//背景色
                    } else {
                        if (mYear == MyCalendarView.getCurrentYear()
                                && mMonth == MyCalendarView.getCurrentMonth()
                                && mDateBean[i][j].getDay() == MyCalendarView.getCurrentDay()) {
                            mTextPaint.setColor(mProperty.getTodayTextColor());
                            canvas.drawCircle(centerX, centerY - DensityUtil.dipToPx(5), mProperty.getTodayCircleRadius(), mCirclePaint);//绘制一个圆
                        } else {
                            mTextPaint.setColor(mProperty.getDayColor());//设置上面文字颜色
                        }
                    }

                    canvas.drawText(mDateBean[i][j].getDay() + "", centerX, centerY, mTextPaint);

                    if (mProperty.isDrawLunar()) {//如果绘制阴历
                        if (MyCalendarView.isSelected(mYear, mMonth, mDateBean[i][j].getDay()))//是选中的 且不是今天
                            mLunarTextPaint.setColor(mProperty.getSelectedTextColor());//选中的文本颜色
                        else if (mDateBean[i][j].isLegal())
                            mLunarTextPaint.setColor(mProperty.getLegalColor());//法定节假日的颜色
                        else
                            mLunarTextPaint.setColor(mProperty.getLunarColor());//设为普通阴历的颜色
                        canvas.drawText(mDateBean[i][j].getLunarStr(), centerX, centerY
                                + mProperty.getSpaceY(), mLunarTextPaint);
                    }

                }
            }
        }

    }

    /**
     * 触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() < mSpaceY)//触摸到标题栏无反应
                return super.onTouchEvent(event);
            int selectX = (int) (event.getX() / mSpaceX);
            int selectY = (int) ((event.getY() - mSpaceY) / mSpaceY);

            if (mDateBean[selectY][selectX] != null) {
                MyCalendarView.setCurrentSelect(mYear, mMonth, mDateBean[selectY][selectX].getDay());//设置当前选择
                invalidate();//重新绘制
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSpaceX = w / 7;
        mSpaceY = h / 7;
    }

    /**
     * 设置日期
     *
     * @param year
     * @param month
     */
    public void setDate(int year, int month, MyCalendarProperty property) {
        mYear = year;
        mMonth = month;
        mProperty = property;//设置一下属性
    }

    /**
     * 文本宽度
     *
     * @return
     */
    private int textWidth(Paint paint, String str) {
        Rect rect = new Rect();
        //返回包围整个字符串的最小的一个Rect区域
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }

}
