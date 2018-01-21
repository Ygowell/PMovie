package com.jam.pmovie.ui.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.ui.detail.MovieDetailFragment;

public class MovieListActivity extends BaseActivity implements MovieListFragment.OnMovieClickListener {

    private ActionBar mActionBar;
    private MovieListFragment mListFragment;
    private MovieDetailFragment mDetailFragment;
    private boolean mOnlyCollected = false;
    private Fragment mCurrentFg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.movie_list);
            mActionBar.setSubtitle(R.string.movie_list_popular);
        }

        mListFragment = new MovieListFragment();
        showMovieListFragment();
    }

    private void showMovieListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!mListFragment.isAdded()) {
            transaction.add(R.id.main_container, mListFragment).commitAllowingStateLoss();
        } else {
            transaction.remove(mDetailFragment).show(mListFragment).commitAllowingStateLoss();
        }
        mCurrentFg = mListFragment;
    }

    private void showMovieDetailFragment(MovieInfo movieInfo) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ExtraName.MOVIE_DATA, movieInfo);
        mDetailFragment.setArguments(bundle);
        transaction.hide(mListFragment).add(R.id.main_container, mDetailFragment)
                .commitAllowingStateLoss();
        mCurrentFg = mDetailFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.refresh:
                mListFragment.refreshList();
                break;
            case R.id.collect:
                mOnlyCollected = !mOnlyCollected;
                item.setIcon(mOnlyCollected ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
                mListFragment.changeCollectState(mOnlyCollected);
                break;
            case R.id.popular:
                mActionBar.setSubtitle(R.string.movie_list_popular);
                mListFragment.changeSortType(Constant.SORT_TYPE_POPULAR);
                break;
            case R.id.top_rated:
                mActionBar.setSubtitle(R.string.movie_list_top_rated);
                mListFragment.changeSortType(Constant.SORT_TYPE_SCORE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(MovieInfo movieInfo) {
        showMovieDetailFragment(movieInfo);
    }

    @Override
    public void onBackPressed() {
        onBackOrClose();
    }

    private void onBackOrClose() {
        if (mCurrentFg instanceof MovieListFragment) {
            finish();
            return;
        }

        if (mCurrentFg instanceof MovieDetailFragment) {
            showMovieListFragment();
        }
    }
}
