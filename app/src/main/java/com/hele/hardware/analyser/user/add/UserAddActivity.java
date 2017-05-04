package com.hele.hardware.analyser.user.add;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserAddActivity extends BaseActivity implements UserAddContract.View {

    @BindView(R.id.et_name)
    EditText nameEt;
    @BindView(R.id.tv_birthday_choose)
    TextView birthdayView;
    @BindView(R.id.rg_sex)
    RadioGroup radioGroup;
    @BindView(R.id.rb_male)
    RadioButton maleRb;
    @BindView(R.id.rb_female)
    RadioButton femaleRb;

    private UserAddContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UserAddPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user_add;
    }

    @Override
    protected int getToolbarContentLayoutId() {
        return 0;
    }

    @Override
    protected String getToolBarTitle() {
        return "添加用户";
    }


    @OnClick({R.id.btn_complete, R.id.tv_birthday_choose})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
                mPresenter.hideKeyboard(this);
                if (mPresenter.saveInfo()) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.tv_birthday_choose:
                mPresenter.chooseBirthday(this);
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
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == R.id.rb_male) {
            return maleRb.getText().toString();
        } else {
            return femaleRb.getText().toString();
        }
    }

    @Override
    public String getBirthday() {
        return birthdayView.getText().toString();
    }

    @Override
    public void showChosenBirthday(String birthday) {
        birthdayView.setText(birthday);
    }

    @Override
    public void showInputError(String et, String message) {
        if (TextUtils.isEmpty(et))
            return;
        EditText editText = null;
        if (et.equals("name")) {
            editText = nameEt;
        } else if (et.equals("birthday")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        if (editText != null) {
            editText.requestFocus();
            editText.setError(message);
        }
    }
}
