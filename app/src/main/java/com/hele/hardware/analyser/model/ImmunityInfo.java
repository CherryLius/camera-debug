package com.hele.hardware.analyser.model;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ImmunityInfo {
    private float mCon;
    private float mTnl;
    private float mCKMB;
    private float mMyo;

    public ImmunityInfo(float con, float tnl, float ckMB, float myo) {
        this.mCon = con;
        this.mTnl = tnl;
        this.mCKMB = ckMB;
        this.mMyo = myo;
    }

    public float getCon() {
        return mCon;
    }

    public float getTnl() {
        return mTnl;
    }

    public float getCKMB() {
        return mCKMB;
    }

    public float getMyo() {
        return mMyo;
    }
}
