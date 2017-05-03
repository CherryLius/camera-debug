package com.hele.hardware.analyser.user.info;

import android.content.Context;
import android.content.Intent;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.hele.hardware.analyser.model.UserInfo;
import com.hele.hardware.analyser.user.add.UserAddActivity;
import com.hele.hardware.analyser.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private List<UserInfo> mUserList;

    public UserInfoPresenter(UserInfoContract.View view) {
        mUserList = new ArrayList<>();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadUsers() {
        for (int i = 0; i < 50; i++) {
            UserInfo info = new UserInfo();
            info.setName(Utils.getRandomString());
            mUserList.add(info);
        }
        Collections.sort(mUserList, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                int length = Math.min(name1.length(), name2.length());
                for (int i = 0; i < length; i++) {
                    if (name1.charAt(i) != name2.charAt(i))
                        return name1.charAt(i) - name2.charAt(i);
                }
                return name1.length() - name2.length();
            }
        });
        mView.showUsers(mUserList);
    }

    @Override
    public SectionItemDecoration.ISectionProvider getSectionProvider() {
        return new SectionItemDecoration.ISectionProvider() {
            @Override
            public long getSectionId(int position) {
                return Character.toUpperCase(mUserList.get(position).getName().charAt(0));
            }

            @Override
            public String getSectionTitle(int position) {
                return mUserList.get(position).getName().substring(0, 1).toUpperCase();
            }
        };
    }

    @Override
    public void gotoUserAdd(Context context) {
        Intent intent = new Intent(context, UserAddActivity.class);
        context.startActivity(intent);
    }
}
