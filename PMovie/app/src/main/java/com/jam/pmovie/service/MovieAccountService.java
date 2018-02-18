package com.jam.pmovie.service;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jam.pmovie.account.Authenticator;

/**
 * Created by jam on 18/2/10.
 */

public class MovieAccountService extends Service {

    private Authenticator mAuthenticator;
    private static final String ACCOUNT_TYPE = "com.jam.pmovie";
    public static final String ACCOUNT_NAME = "sync";

    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
