package com.jam.pmovie.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.ui.detail.MovieDetailActivity;
import com.jam.pmovie.ui.detail.MovieDetailFragment;
import com.jam.pmovie.ui.setting.SettingActivity;

public class MovieListActivity extends BaseActivity implements MovieListFragment.OnMovieClickListener,
        MovieDetailFragment.OnDetailActionListener {

    private ActionBar mActionBar;
    private MovieListFragment mListFragment;
    private MovieDetailFragment mDetailFragment;
    private boolean mIsTwoPanel = false;
    private final static int REQ_MOVIE_DETAIL = 100;

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

        if (findViewById(R.id.movie_detail_container) != null) {
            mIsTwoPanel = true;
        } else {
            mIsTwoPanel = false;
        }
        showMovieListFragment();
    }

    private void showMovieListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mListFragment = new MovieListFragment();
        transaction.add(R.id.main_container, mListFragment).commitAllowingStateLoss();
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
            case R.id.settings:
                startActivityForResult(new Intent(this, SettingActivity.class),
                        Constant.REQ_SETTING);
                break;
            case R.id.collected:
                mActionBar.setSubtitle(R.string.movie_list_collected);
                mListFragment.changeCollectState(true);
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
        if (mIsTwoPanel) {
            showMovieDetailFragment(movieInfo);
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(Constant.ExtraName.MOVIE_DATA, movieInfo);
            startActivityForResult(intent, REQ_MOVIE_DETAIL);
        }
    }

    @Override
    public void onReturnFirstMovie(MovieInfo movieInfo) {
        if (mIsTwoPanel) {
            showMovieDetailFragment(movieInfo);
        }
    }

    private void showMovieDetailFragment(MovieInfo movieInfo) {
        if (mDetailFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.ExtraName.MOVIE_DATA, movieInfo);
            mDetailFragment.setArguments(bundle);
            transaction.add(R.id.movie_detail_container, mDetailFragment).commitAllowingStateLoss();
        } else {
            mDetailFragment.showMovieDetail(movieInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_MOVIE_DETAIL) { // 电影被收藏
                mListFragment.getMovieListFromDb();
            }
        }
    }

    @Override
    public void onCollectClicked(boolean isCollect) {
        mListFragment.getMovieListFromDb();
    }
}
