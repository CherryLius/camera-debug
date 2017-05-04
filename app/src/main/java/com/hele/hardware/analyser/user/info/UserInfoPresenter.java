package com.hele.hardware.analyser.user.info;

import android.content.Context;
import android.content.Intent;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.github.promeg.pinyinhelper.Pinyin;
import com.hele.hardware.analyser.model.UserInfo;
import com.hele.hardware.analyser.user.add.UserAddActivity;
import com.hele.hardware.analyser.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/2.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private List<UserInfo> mUserList;

    public UserInfoPresenter(UserInfoContract.View view) {
        mUserList = new ArrayList<>();
        Pinyin.init(Pinyin.newConfig());
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadUsers() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            UserInfo info = new UserInfo();
            info.setName(random.nextInt(2) == 0
                    ? Utils.getRandomEnglish()
                    : Utils.getRandomChinese());
            mUserList.add(info);
        }
        Collections.sort(mUserList, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                String spelling1 = Pinyin.toPinyin(name1, "").toUpperCase();
                String spelling2 = Pinyin.toPinyin(name2, "").toUpperCase();
                if (spelling1.charAt(0) == spelling2.charAt(0)) {
                    if (Pinyin.isChinese(name1.charAt(0))
                            && !Pinyin.isChinese(name2.charAt(0))) {
                        return 1;
                    } else if (!Pinyin.isChinese(name1.charAt(0))
                            && Pinyin.isChinese(name2.charAt(0))) {
                        return -1;
                    }
                }

                int length = Math.min(spelling1.length(), spelling2.length());
                for (int i = 0; i < length; i++) {
                    if (spelling1.charAt(i) != spelling2.charAt(i))
                        return spelling1.charAt(i) - spelling2.charAt(i);
                }
                return spelling1.length() - spelling2.length();
            }
        });
        mView.showUsers(mUserList);
    }

    @Override
    public SectionItemDecoration.ISectionProvider getSectionProvider() {
        return new SectionItemDecoration.ISectionProvider() {
            @Override
            public long getSectionId(int position) {
                String name = mUserList.get(position).getName();
                String spelling = Pinyin.toPinyin(name, "");
                return Character.toUpperCase(spelling.charAt(0));
            }

            @Override
            public String getSectionTitle(int position) {
                String name = mUserList.get(position).getName();
                String spelling = Pinyin.toPinyin(name, "");
                return spelling.substring(0, 1).toUpperCase();
            }
        };
    }

    @Override
    public void gotoUserAdd(Context context) {
        Intent intent = new Intent(context, UserAddActivity.class);
        context.startActivity(intent);
    }
}
