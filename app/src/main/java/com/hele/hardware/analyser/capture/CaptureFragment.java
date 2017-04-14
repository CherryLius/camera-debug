package com.hele.hardware.analyser.capture;

import android.Manifest;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ViewStubCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.cherry.library.ui.view.CountDownView;
import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.util.Utils;
import com.ui.picker.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.hele.hardware.analyser.common.Constants.PERMISSION_REQUEST_CODE_CAMERA;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CaptureFragment extends BaseFragment implements CaptureContract.View {

    private static final String TAG = "CaptureFragment";

    @BindView(R.id.layout_capture)
    ViewGroup captureLayout;
    @BindView(R.id.layout_picture)
    ViewGroup pictureLayout;
    @BindView(R.id.stub_result)
    ViewStubCompat resultViewStub;
    @BindView(R.id.count_down_view)
    CountDownView countDownView;
    @BindView(R.id.cb_alarm)
    AppCompatCheckBox alarmCheckBox;

    StubViewHolder mStubHolder;

    @BindView(R.id.gl_surface)
    GLSurfaceView glSurfaceView;
    CaptureRenderer captureRenderer;

    private CaptureContract.Presenter mPresenter;

    private TimePicker timePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    void init() {
        alarmCheckBox.setChecked(false);
        captureRenderer = new CaptureRenderer(getContext(), glSurfaceView);
        new CapturePresenter(this, captureRenderer);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
            captureRenderer.resume();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE_CAMERA);
        }
        countDownView.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        captureRenderer.pause();
        countDownView.pause();
    }

    @Override
    public void setPresenter(@NonNull CaptureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick({R.id.iv_capture, R.id.iv_switch, R.id.iv_analyse, R.id.iv_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_capture:
                mPresenter.capture();
                break;
            case R.id.iv_switch:
                showToast("not impl!");
                break;
            case R.id.iv_analyse:
                if (mStubHolder == null) {
                    mStubHolder = new StubViewHolder(resultViewStub.inflate());
                } else {
                    mStubHolder.show();
                }
                break;
            case R.id.iv_cancel:
                captureRenderer.setFilter(CaptureRenderer.STATE_CAPTURE);
                updateController(true);
                break;
        }
    }

    @OnCheckedChanged(R.id.cb_alarm)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showTimePicker();
        } else {
            countDownView.setVisibility(View.GONE);
            countDownView.stop();
        }
    }

    @Override
    public void showToast(String text) {
        Utils.showToastOnUiThread(getActivity(), text);
    }

    @Override
    public void showBitmap(final Bitmap bitmap) {
        captureRenderer.setBitmap(bitmap);
        captureRenderer.setFilter(CaptureRenderer.STATE_PICTURE);
    }

    @Override
    public void updateController(final boolean isInCapture) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    captureLayout.setVisibility(isInCapture ? View.VISIBLE : View.GONE);
                    pictureLayout.setVisibility(!isInCapture ? View.VISIBLE : View.GONE);
                }
            });
    }

    @Override
    public boolean isInCapture() {
        return captureRenderer.isInCapture();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        captureRenderer.destroy();
    }

    @Override
    public boolean onBackPressed() {
        if (isInCapture()) {
            return false;
        } else {
            if (mStubHolder != null && mStubHolder.isShowing()) {
                mStubHolder.hide();
            } else {
                updateController(true);
                captureRenderer.setFilter(CaptureRenderer.STATE_CAPTURE);
            }
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CAMERA:
                if (!Utils.checkPermissionGrantResults(grantResults)) {
                    showToast("没有摄像头权限");
                    getActivity().finish();
                }
                break;
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
                    countDownView.setVisibility(View.VISIBLE);
                    countDownView.setCountDownTime(Integer.valueOf(hour), Integer.valueOf(minute), 0);
                    hideTimePicker();
                    countDownView.start();
                }
            });
        }
        timePicker.show();
    }

    private void hideTimePicker() {
        if (timePicker != null && timePicker.isShowing()) {
            timePicker.dismiss();
        }
    }

    class StubViewHolder {

        View rootView;
        @BindView(R.id.tv_result)
        AppCompatTextView textView;

        public StubViewHolder(View view) {
            rootView = view;
            ButterKnife.bind(this, view);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        @OnClick({R.id.tv_close, R.id.btn_confirm})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_close:
                    rootView.setVisibility(View.GONE);
                    break;
                case R.id.btn_confirm:
                    rootView.setVisibility(View.GONE);
            }
        }

        void show() {
            if (rootView != null)
                rootView.setVisibility(View.VISIBLE);
        }

        void hide() {
            if (rootView != null)
                rootView.setVisibility(View.GONE);
        }

        boolean isShowing() {
            return rootView != null && rootView.getVisibility() == View.VISIBLE;
        }
    }
}
