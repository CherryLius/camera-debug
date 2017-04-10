package com.hele.hardware.analyser.capture;

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

/**
 * Created by Administrator on 2017/4/6.
 */

public class CaptureFragment extends BaseFragment implements CaptureContract.View {

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
        captureRenderer.resume();
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

    @OnClick(R.id.debug_btn_capture)
    public void click(View view) {
        mPresenter.capture();
    }

    @Override
    public void showToast(String text) {
        Utils.showToastOnUiThread(getActivity(), text);
    }
}
