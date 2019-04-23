package top.systemsec.mycalendar.custom_view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import top.systemsec.mycalendar.R;
import top.systemsec.mycalendar.adapter.ListViewAdapter;
import top.systemsec.mycalendar.utils.DensityUtil;

public class SpinnerView extends LinearLayout {

    private static final String TAG = "SpinnerView";

    private View mLayout;

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private TextView mSpinnerText;

    private Integer mNowData;//当前
    private Integer[] mListData;

    public SpinnerView(Context context) {
        super(context);
        init(context);
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_spinner_head, this);
        mSpinnerText = findViewById(R.id.textView);

        if (mNowData != null) {
            if (mNowData < 10)
                mSpinnerText.setText("0" + mNowData);//设置名称
            else
                mSpinnerText.setText(mNowData + "");//设置名称
        }

    }

    public void setData(Integer[] data) {
        this.mListData = data;
        mAdapter = new ListViewAdapter(getContext(), R.layout.item_string, data); //默认设置下拉框的标题为数据的第一个
        mSpinnerText.setOnClickListener((v) -> {
                    initView();
                    mPopupWindow.showAsDropDown(mSpinnerText);
                }
        );
    }


    private void initView() {
        if (mLayout == null) {
            mLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom_spinner, null);
            // 实例化listView
            mListView = mLayout.findViewById(R.id.listView);
            // 设置listView的适配器
            mListView.setAdapter(mAdapter);
            // listView的item点击事件
            mListView.setOnItemClickListener((AdapterView<?> arg0, View arg1, int arg2, long arg3) -> {
                        mNowData = mListData[arg2];

                        if (mOnSelectedListener != null)
                            mOnSelectedListener.onSelected(mNowData);//设置当前数据

                        if (mNowData < 10)
                            mSpinnerText.setText("0" + mNowData);//设置名称
                        else
                            mSpinnerText.setText(mNowData + "");//设置名称

                        if (mPopupWindow != null)
                            mPopupWindow.dismiss(); //弹框消失
                    }
            );
        }

        if (mPopupWindow == null) {
            // 实例化一个PopupWindow对象
            mPopupWindow = new PopupWindow(getContext());
            // 设置弹框的宽度为布局文件的宽

            mPopupWindow.setWidth(getWidth());
            // 高度设置的300
            mPopupWindow.setHeight(DensityUtil.dipToPx(300));
            // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            // 设置点击弹框外部，弹框消失
            mPopupWindow.setOutsideTouchable(true);
            // 设置焦点
            mPopupWindow.setFocusable(true);
            // 设置所在布局
            mPopupWindow.setContentView(mLayout);
            // 设置弹框出现的位置，在v的正下方横轴偏移textView的宽度
        }

    }

    /**
     * 设置当前
     *
     * @param nowData
     */
    public void setNowData(Integer nowData) {
        mNowData = nowData;
        if (mSpinnerText != null) {
            if (mNowData < 10)
                mSpinnerText.setText("0" + mNowData);//设置名称
            else
                mSpinnerText.setText(mNowData + "");//设置名称
        }
    }

    /**
     * 得到当前数据
     *
     * @return
     */
    public Integer getNowData() {
        return mNowData;
    }

    /**
     * 选择监听
     */
    public interface OnSelectedListener {
        void onSelected(int data);
    }

    private OnSelectedListener mOnSelectedListener;

    /**
     * 设置选择监听
     *
     * @param onSelectedListener
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        mOnSelectedListener = onSelectedListener;
    }
}