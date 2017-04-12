package com.hele.hardware.analyser.capture;

import android.Manifest;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hele.hardware.analyser.common.Constants.PERMISSION_REQUEST_CODE_CAMERA;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CaptureFragment extends BaseFragment implements CaptureContract.View {

    private static final String TAG = "CaptureFragment";

    @BindView(R.id.debug_controller)
    ViewGroup debugViewGroup;
    @BindView(R.id.debug_btn_capture)
    AppCompatButton captureBtn;
    @BindView(R.id.gl_surface)
    GLSurfaceView glSurfaceView;
    CaptureRenderer captureRenderer;

    private CaptureContract.Presenter mPresenter;

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

    }

    @Override
    public void onPause() {
        super.onPause();
        captureRenderer.pause();
    }

    @Override
    public void setPresenter(@NonNull CaptureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick({R.id.debug_btn_capture, R.id.debug_btn_switch})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.debug_btn_capture:
                mPresenter.capture();
                break;
            case R.id.debug_btn_switch:
                //captureRenderer.setFilter(CaptureRenderer.STATE_CAPTURE);
                showToast("not impl!");
                break;
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
            captureRenderer.setFilter(CaptureRenderer.STATE_CAPTURE);
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
}
