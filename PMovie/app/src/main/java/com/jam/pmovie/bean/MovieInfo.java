package com.jam.pmovie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jam on 17/9/4.
 */

public class MovieInfo implements Parcelable {
    /**
     * voteCount : 4064
     * id : 211672
     * video : false
     * voteAverage : 6.4
     * title : Minions
     * popularity : 197.822758
     * posterPath : /q0R4crx2SehcEEQEkYObktdeFy.jpg
     * original_language : en
     * original_title : Minions
     * genre_ids : [10751,16,12,35]
     * backdrop_path : /uX7LXnsC7bZJZjn048UCOwkPXWJ.jpg
     * adult : false
     * overview : Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.
     * releaseDate : 2015-06-17
     */

    @SerializedName("vote_count")
    private int voteCount;
    private long id;
    private boolean video;
    @SerializedName("vote_average")
    private String voteAverage;
    private String title;
    private String popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String orgLanguage;
    @SerializedName("original_title")
    private String orgTitle;
    @SerializedName("backdrop_path")
    private String backdropPath;
    private boolean adult;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    private boolean collected; // 是否收藏（本地使用）
    private boolean loaded; // 是否请求过时长、预告片和评论
    private int sortType; // 0: 最受欢迎 1: 最佳评分

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public String getOrgLanguage() {
        return orgLanguage;
    }

    public void setOrgLanguage(String orgLanguage) {
        this.orgLanguage = orgLanguage;
    }

    public String getOrgTitle() {
        return orgTitle;
    }

    public void setOrgTitle(String original_title) {
        this.orgTitle = original_title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdropPath = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteCount);
        dest.writeLong(this.id);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeString(this.voteAverage);
        dest.writeString(this.title);
        dest.writeString(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.orgLanguage);
        dest.writeString(this.orgTitle);
        dest.writeString(this.backdropPath);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    public MovieInfo() {
    }

    protected MovieInfo(Parcel in) {
        this.voteCount = in.readInt();
        this.id = in.readLong();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readString();
        this.title = in.readString();
        this.popularity = in.readString();
        this.posterPath = in.readString();
        this.orgLanguage = in.readString();
        this.orgTitle = in.readString();
        this.backdropPath = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * 0: 最受欢迎 1: 最佳评分
     */
    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }
}
