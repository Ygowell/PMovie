package com.jam.pmovie.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.http.HttpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jam on 17/8/19.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private Context mContext;
    private List<MovieInfo> mMovieInfoList;
    private OnMovieItemClickListener mMovieItemClickListener;

    public MovieListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<MovieInfo> movieInfoList) {
        mMovieInfoList = movieInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnMovieItemClickListener listener) {
        mMovieItemClickListener = listener;
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(R.drawable.default_movie_icon)
                .error(R.drawable.default_movie_icon);
        Glide.with(mContext)
                .load(HttpUtils.MOVIE_PIC_URL + mMovieInfoList.get(position).getPosterPath())
                .apply(requestOptions)
                .into(holder.mMovieCoverIv);
    }

    @Override
    public int getItemCount() {
        return mMovieInfoList == null ? 0 : mMovieInfoList.size();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_movie_cover)
        public ImageView mMovieCoverIv;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMovieItemClickListener != null) {
                        mMovieItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnMovieItemClickListener {
        void onItemClick(int pos);
    }
}
