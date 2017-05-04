package com.hele.hardware.analyser.behavior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hele.hardware.analyser.base.BaseFragment;
import com.hele.hardware.analyser.R;
import com.ui.picker.TimePicker;

/**
 * Created by Administrator on 2017/4/21.
 */

public class StepFragment extends BaseFragment implements View.OnClickListener {

    private static final String EXTRA_STEP = "extra_step";
    private int mStep = 0;
    private OnStepListener mListener;

    private TimePicker timePicker;
    private TimePicker.OnTimePickListener mTimePickListener;

    //@BindView(R.id.before)
    Button beforeButton;
    //@BindView(R.id.next)
    Button nextButton;
    //@BindView(R.id.confirm)
    Button confirmButton;

    public static StepFragment newInstance(int step) {
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
        ViewGroup viewGroup = (ViewGroup) view.findViewWithTag("step_" + mStep);
        if (viewGroup != null) {
            viewGroup.setVisibility(View.VISIBLE);
            beforeButton = (Button) viewGroup.findViewById(R.id.before);
            nextButton = (Button) viewGroup.findViewById(R.id.next);
            confirmButton = (Button) viewGroup.findViewById(R.id.confirm);
            if (beforeButton != null)
                beforeButton.setOnClickListener(this);
            if (nextButton != null)
                nextButton.setOnClickListener(this);
            if (confirmButton != null)
                confirmButton.setOnClickListener(this);
        }
    }

    //@OnClick({R.id.next, R.id.before, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.before:
                executeBefore();
                break;
            case R.id.next:
                executeNext();
                break;
            case R.id.confirm:
                executeConfirm();
                break;
        }
    }

    private void executeBefore() {
        if (mListener != null)
            mListener.before(mStep);
    }

    private void executeNext() {
        if (mListener != null)
            mListener.next(mStep);
    }

    private void executeConfirm() {
        if (mStep == 1) {
            showTimePicker();
        }
    }

    private void showTimePicker() {
        if (timePicker == null) {
            timePicker = new TimePicker(getActivity(), TimePicker.HOUR_24);
            timePicker.setRangeStart(0, 0);//00:00
            timePicker.setRangeEnd(23, 59);//23:59
            timePicker.setSelectedItem(0, 0);
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
                    confirmButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
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
}
