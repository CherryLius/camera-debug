package com.hele.hardware.analyser.user.add;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hele.hardware.analyser.dao.UserInfoDaoHelper;
import com.hele.hardware.analyser.model.UserInfo;
import com.hele.hardware.analyser.util.HLog;
import com.hele.hardware.analyser.util.Utils;

/**
 * Created by Administrator on 2017/5/3.
 */

public class UserAddPresenter implements UserAddContract.Presenter {

    private static final String TAG = "UserAddPresenter";
    private UserAddContract.View mView;

    public UserAddPresenter(UserAddContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public boolean saveInfo() {
        String name = mView.getName();
        String sex = mView.getSex();
        String age = mView.getAge();
        HLog.i(TAG, "name=" + name + ",sex=" + sex + ",age=" + age);
        if (invalidCheck(name, "name") && invalidCheck(age, "age")) {/*&& invalidCheck(sex, "sex")*/
            UserInfo info = new UserInfo();
            info.setName(name);
            info.setSex(sex.equals("女") ? 1 : 0);
            info.setAge(Integer.valueOf(age));
            long id = UserInfoDaoHelper.instance().add(info);
            if (id != -1)
                return true;
        }
        return false;
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
        if (TextUtils.isEmpty(content)) {
            mView.showInputError(view, "不能为空");
            return false;
        } else {
            /*if (view.equals("sex")
                    && (!content.equals("男") && !content.equals("女"))) {
                mView.showInputError(view, "输入性别 男 或 女");
                return false;
            }*/
            mView.showInputError(view, null);
            return true;
        }
    }
}
