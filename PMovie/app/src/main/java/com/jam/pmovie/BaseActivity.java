package com.jam.pmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by jam on 17/8/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        onProxyCreate(savedInstanceState);
    }

    protected void onProxyCreate(@Nullable Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

}
