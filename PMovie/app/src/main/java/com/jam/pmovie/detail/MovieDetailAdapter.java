package com.jam.pmovie.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jam on 18/1/11.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder> {



    @Override
    public MovieDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MovieDetailViewHolder extends RecyclerView.ViewHolder {

        public MovieDetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
