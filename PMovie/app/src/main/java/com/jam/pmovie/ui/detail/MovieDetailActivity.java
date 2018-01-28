package com.jam.pmovie.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.data.MovieCpHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MovieDetailActivity extends BaseActivity {

    private static final String TAG = "MovieDetailActivity";

    private MovieInfo mMovieInfo;
    private boolean mIsCollect;
    private MovieDetailFragment mDetailFragment;
    private boolean mIsOrgCollect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);

        mMovieInfo = getIntent().getParcelableExtra(Constant.ExtraName.MOVIE_DATA);
        if (mMovieInfo == null) {
            finish();
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.movie_detail);
        }

        mIsCollect = mMovieInfo.isCollected();
        mIsOrgCollect = mIsCollect;

        mDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ExtraName.MOVIE_DATA, mMovieInfo);
        mDetailFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.movie_detail_container, mDetailFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        menu.findItem(R.id.collect_detail).setIcon(mIsCollect ? R.drawable.ic_favorite
                : R.drawable.ic_unfavorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                setReturnResult();
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        } else if (itemId == R.id.collect_detail) {
            mIsCollect = !mIsCollect;
            MovieCpHelper.updateMovieCollectState(mMovieInfo.getId(),
                    mIsCollect ? 1 : 0).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) { // Fail to update
                            mIsCollect = !mIsCollect;
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                item.setIcon(mIsCollect ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
                            } else { // Fail to update
                                mIsCollect = !mIsCollect;
                            }
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setReturnResult();
    }

    private void setReturnResult() {
        if (mIsOrgCollect != mIsCollect) {
            setResult(RESULT_OK);
        }
    }
}
