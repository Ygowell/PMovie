package com.jam.pmovie.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.common.SyncUtils;
import com.jam.pmovie.data.PrefHelper;
import com.jam.pmovie.data.PrefKey;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jam on 18/1/31.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.switch_notification)
    Switch mSwitch;
    @BindView(R.id.tv_update_frequency)
    TextView mFrequencyTv;

    private static final int[] FREQUENCY = {1, 6, 12, 24};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);

        mSwitch.setChecked(PrefHelper.getNotificationOpen());
        mFrequencyTv.setText(PrefHelper.getString(PrefKey.UPDATE_FREQUENCY_TEXT, getString(R.string.every_day)));

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefHelper.saveNotificationOpen(isChecked);
            }
        });
    }

    @OnClick(R.id.update_frequency_layout)
    public void onFrequencyClicked() {
        showFrequencyDialog();
    }

    private void showFrequencyDialog() {
        final String items[] = getResources().getStringArray(R.array.frequency_option);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_update_frequency);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mFrequencyTv.setText(items[which]);
                PrefHelper.saveString(PrefKey.UPDATE_FREQUENCY_TEXT, items[which]);
                PrefHelper.saveInt(PrefKey.UPDATE_FREQUENCY_NUM, FREQUENCY[which]);
                SyncUtils.changePeriodicSyncFrequency();
            }
        });

        builder.create().show();
    }
}
