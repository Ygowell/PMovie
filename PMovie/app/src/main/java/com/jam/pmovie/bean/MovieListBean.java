package com.jam.pmovie.bean;

import java.util.List;

/**
 * Created by jam on 17/8/16.
 */

public class MovieListBean {
    /**
     * page : 1
     * total_results : 19616
     * total_pages : 981
     * results : [{"vote_count":4064,"id":211672,"video":false,"vote_average":6.4,"title":"Minions","popularity":197.822758,"poster_path":"/q0R4crx2SehcEEQEkYObktdeFy.jpg","original_language":"en","original_title":"Minions","genre_ids":[10751,16,12,35],"backdrop_path":"/uX7LXnsC7bZJZjn048UCOwkPXWJ.jpg","adult":false,"overview":"Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.","release_date":"2015-06-17"}]
     */

    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieInfo> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieInfo> getResults() {
        return results;
    }

    public void setResults(List<MovieInfo> results) {
        this.results = results;
    }
}
