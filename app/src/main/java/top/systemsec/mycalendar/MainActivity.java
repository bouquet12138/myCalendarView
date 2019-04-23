package top.systemsec.mycalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import top.systemsec.mycalendar.custom_view.MyCalendarView;
import top.systemsec.mycalendar.custom_view.SpinnerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FourActivity";

    private MyCalendarView mMyCalendarView;
    private ImageView mTodayImg;
    private int mYear;
    private int mMonth;
    private TextView mWeekText;
    private ImageView mLeftArrow;
    private ImageView mRightArrow;

    private SpinnerView mYearSpinner;
    private SpinnerView mMonthSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String[] weekTitles = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d(TAG, "initData: ");

        Integer[] years = new Integer[199];
        Integer[] months = new Integer[12];

        for (int i = 0; i < years.length; i++)
            years[i] = 1901 + i;

        for (int i = 0; i < months.length; i++)
            months[i] = 1 + i;

        mYearSpinner.setNowData(mYear);
        mMonthSpinner.setNowData(mMonth);

        mYearSpinner.setData(years);
        mMonthSpinner.setData(months);

        mWeekText.setText("星期" + weekTitles[week]);//星期几
    }

    /**
     * 初始化View
     */
    private void initView() {

        mMyCalendarView = findViewById(R.id.myCalendarView);
        mTodayImg = findViewById(R.id.todayImg);
        mWeekText = findViewById(R.id.weekText);
        mLeftArrow = findViewById(R.id.leftArrow);
        mRightArrow = findViewById(R.id.rightArrow);

        mYearSpinner = findViewById(R.id.yearSpinner);
        mMonthSpinner = findViewById(R.id.monthSpinner);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mMyCalendarView.setSelectListener(new MyCalendarView.OnSelectListener() {
            @Override
            public void change(int year, int month) {
                mYearSpinner.setNowData(year);
                mMonthSpinner.setNowData(month);
            }

            @Override
            public void onSelected(int year, int month, int day) {
                Log.d(TAG, "onSelected: year " + year + " month " + month + " day " + day);
            }
        });

        mTodayImg.setOnClickListener((v) -> {//点击监听
            mMyCalendarView.setCurrentPosition(mYear, mMonth);
        });

        mLeftArrow.setOnClickListener((v) -> {
            mMyCalendarView.left();//向左移动
        });

        mRightArrow.setOnClickListener((v) -> {
            mMyCalendarView.right();//向右移动
        });

        mYearSpinner.setOnSelectedListener((year) ->
                mMyCalendarView.setCurrentPosition(year, mMonthSpinner.getNowData())
        );

        mMonthSpinner.setOnSelectedListener((month) ->
                mMyCalendarView.setCurrentPosition(mYearSpinner.getNowData(), month)
        );

    }

}
