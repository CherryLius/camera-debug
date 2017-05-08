package com.hele.hardware.analyser.behavior;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseFragment;
import com.hele.hardware.analyser.user.info.UserInfoActivity;
import com.ui.picker.TimePicker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/4/21.
 */

public class StepFragment extends BaseFragment implements View.OnClickListener {

    private static final String EXTRA_STEP = "extra_step";
    private static final int REQUEST_USER_CODE = 101;

    public static final int STEP_CONFIRM = 0;
    public static final int STEP_USER_ADD = 1;
    public static final int STEP_TIME_SETTING = 2;


    @IntDef({STEP_CONFIRM, STEP_USER_ADD, STEP_TIME_SETTING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Step {
    }

    private int mStep = 0;
    private OnStepListener mListener;

    private TimePicker timePicker;
    private TimePicker.OnTimePickListener mTimePickListener;

    private TextView mContentView;
    private Button mBeforeButton;
    private Button mNextButton;
    private Button mConfirmButton;

    public static StepFragment newInstance(@Step int step) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_STEP, step);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mStep = args.getInt(EXTRA_STEP, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_setp, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBeforeButton = (Button) view.findViewById(R.id.before);
        mNextButton = (Button) view.findViewById(R.id.next);
        mConfirmButton = (Button) view.findViewById(R.id.confirm);
        mContentView = (TextView) view.findViewById(R.id.tv_step);
        if (mBeforeButton != null)
            mBeforeButton.setOnClickListener(this);
        if (mNextButton != null)
            mNextButton.setOnClickListener(this);
        if (mConfirmButton != null)
            mConfirmButton.setOnClickListener(this);

        switch (mStep) {
            case STEP_CONFIRM:
                mContentView.setText("请确认检测板摆放正确");
                break;
            case STEP_USER_ADD:
                mContentView.setText("请选择检测对象");
                mNextButton.setText("选择");
                break;
            case STEP_TIME_SETTING:
                mContentView.setText("请设定时间");
                mNextButton.setText("设置");
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before:
                executeBefore();
                break;
            case R.id.next:
                executeNext();
                break;
            case R.id.confirm:
                break;
        }
    }

    private void executeBefore() {
        if (mListener != null)
            mListener.before(mStep);
    }

    private void executeNext() {
        switch (mStep) {
            case STEP_CONFIRM:
                if (mListener != null)
                    mListener.next(mStep);
                break;
            case STEP_USER_ADD:
                UserInfoActivity.toActivityForResult(getActivity(), "type_select", REQUEST_USER_CODE);
                break;
            case STEP_TIME_SETTING:
                showTimePicker();
                break;
        }
    }

    private void showTimePicker() {
        if (timePicker == null) {
            timePicker = new TimePicker(getActivity(), TimePicker.HOUR_24);
            timePicker.setRangeStart(0, 1);//00:01
            timePicker.setRangeEnd(23, 59);//23:59
            timePicker.setSelectedItem(0, 15);
            timePicker.setTopLineVisible(false);
            timePicker.setLineVisible(false);
            timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    int h = Integer.valueOf(hour);
                    int m = Integer.valueOf(minute);
                    if (h == 0 && m == 0) {
                        return;
                    }
                    if (mTimePickListener != null)
                        mTimePickListener.onTimePicked(hour, minute);
                }
            });
        }
        timePicker.show();
    }

    public void setOnTimePickListener(TimePicker.OnTimePickListener listener) {
        mTimePickListener = listener;
    }

    public void setStepListener(OnStepListener listener) {
        mListener = listener;
    }

    public interface OnStepListener {
        void before(int currentStep);

        void next(int currentStep);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USER_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (mListener != null)
                    mListener.next(mStep);
            } else {

            }
        }
    }
}
