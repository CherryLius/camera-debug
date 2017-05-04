package com.hele.hardware.analyser.user.add;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hele.hardware.analyser.dao.UserInfoDaoHelper;
import com.hele.hardware.analyser.model.UserInfo;
import com.hele.hardware.analyser.util.Utils;
import com.ui.picker.DateTimePicker;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/5/3.
 */

public class UserAddPresenter implements UserAddContract.Presenter {

    private static final String TAG = "UserAddPresenter";
    private UserAddContract.View mView;

    private DateTimePicker mDateTimePicker;

    public UserAddPresenter(UserAddContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public boolean saveInfo() {
        String name = mView.getName();
        String sex = mView.getSex();
        String birthday = mView.getBirthday();
        if (invalidCheck(name, "name") && invalidCheck(birthday, "birthday")) {
            UserInfo info = new UserInfo();
            info.setName(name);
            info.setSex(sex.equals("女") ? 1 : 0);
            info.setBirthday(birthday);
            info.setAge(getAgeFromBirthday(birthday));
            long id = UserInfoDaoHelper.instance().add(info);
            if (id != -1)
                return true;
        }
        return false;
    }

    @Override
    public void chooseBirthday(Activity activity) {
        if (mDateTimePicker == null) {
            mDateTimePicker = new DateTimePicker(activity, DateTimePicker.YEAR_MONTH, DateTimePicker.NONE);
            mDateTimePicker.setDateRangeStart(1949, 1);
            Calendar calendar = Calendar.getInstance();//"GMT"
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH) + 1;
            //int endDay = calendar.get(Calendar.DAY_OF_MONTH);
            mDateTimePicker.setDateRangeEnd(endYear, endMonth);
            mDateTimePicker.setSelectedItem(endYear, endMonth, 0, 0);
            mDateTimePicker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String hour, String minute) {
                    String birthday = year + "-" + month;
                    mView.showChosenBirthday(birthday);
                }
            });
        }
        mDateTimePicker.show();
    }

    @Override
    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager ims = Utils.getSystemService(activity, Context.INPUT_METHOD_SERVICE);
            ims.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean invalidCheck(String content, String view) {
        if (view.equals("birthday") &&
                (TextUtils.isEmpty(content) || !content.contains("-"))) {
            mView.showInputError(view, "请选择出生日期");
            return false;
        } else if (view.equals("name")) {
            if (TextUtils.isEmpty(content)) {
                mView.showInputError(view, "不能为空");
                return false;
            } else {
                mView.showInputError(view, null);
                return true;
            }
        }
        return true;
    }

    private int getAgeFromBirthday(String birthday) {
        int index = birthday.indexOf('-');
        String year = birthday.substring(0, index);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int age = currentYear - Integer.valueOf(year);
        return age;
    }
}
