package com.jam.pmovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by jam on 18/1/21.
 */

public abstract class BaseFragment extends Fragment {

    private View rootView;

    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        onProxyCreateView();
        return rootView;
    }

    protected void onProxyCreateView() {
    }

   protected View getCreatedView() {
        return rootView;
   }
}
