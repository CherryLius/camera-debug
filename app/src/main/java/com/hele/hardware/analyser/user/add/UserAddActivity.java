package com.hele.hardware.analyser.user.add;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hele.hardware.analyser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserAddActivity extends AppCompatActivity implements UserAddContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView titleView;
    @BindView(R.id.et_name)
    EditText nameEt;
    @BindView(R.id.et_sex)
    EditText sexEt;
    @BindView(R.id.et_age)
    EditText ageEt;

    private UserAddContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        ButterKnife.bind(this);
        new UserAddPresenter(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        titleView.setText("添加用户");
    }

    @OnClick({R.id.btn_save, R.id.iv_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (mPresenter.saveInfo()) {
                    finish();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(@NonNull UserAddContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getName() {
        return nameEt.getText().toString();
    }

    @Override
    public String getSex() {
        return sexEt.getText().toString();
    }

    @Override
    public String getAge() {
        return ageEt.getText().toString();
    }

    @Override
    public void showInputError(String et, String message) {
        if (TextUtils.isEmpty(et))
            return;
        EditText editText = null;
        if (et.equals("name")) {
            editText = nameEt;
        } else if (et.equals("age")) {
            editText = ageEt;
        } else if (et.equals("sex")) {
            editText = sexEt;
        }
        if (editText != null) {
            editText.requestFocus();
            editText.setError(message);
        }
    }
}
