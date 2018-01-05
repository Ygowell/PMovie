package com.jam.pmovie.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jam on 18/1/3.
 */

public class CommentInfo {
    /**
     * id : 391713
     * page : 1
     * results : [{"id":"5a24615ac3a3680b9a0d882a","author":"Innovator","content":"This movie was ok (it wasn't boring nor was it very entertaining). At first I didn't understand the point it was trying to make. Was it you can shortcut your way to your dreams if you lie, cheat, and steal (as that was exactly what she did)? It wasn't about the pressures of class division (as a review I read states), as no one treated her as any differently regardless of her social status and any division she felt was just in her head. Her anxiety against her parents, the school, and the upper class of her school turned out to be non-issues, and when she got to where or what she wants, she just finds that the grass never gets greener for her once she was on the other side. In fact, she just becomes resented by those she abandons to get there and ultimately ends up longing for the side she left. \r\n\r\nIf anything the movie was saying that those who are rich aren't as unexclusive as those without think, and they are just like everyone else. Just as the main protagonist was rebelling against her life to gain acceptance, so were the kids of the rich in that they are also just seeking acceptance. That doing anything to get your dreams won't lead to satisfaction as life on the other side isn't any better, you just end up hurting others by getting there that way, and in hindsight blinded you from what you already had. \r\n\r\nI guess the movie had something to say after all.\r\n\r\n★★½ - Not dull, nor was it very entertaining.","url":"https://www.themoviedb.org/review/5a24615ac3a3680b9a0d882a"},{"id":"5a46240e9251411fab0942d8","author":"Gimly","content":"I think _Lady Bird_ is my film for 2017 where me and everyone else on the planet just straight up do not see eye to eye. At no point during _Lady Bird_ did I feel drawn in. I genuinely did not enjoy my time with Greta Gerwig's directorial debut. I haven't come across a single other person who feels the same, but I must be honest to my experience.\r\n\r\nNormally this is where I would say something along the lines of, \"It's just because this isn't my sort of movie\" except that last year _Edge of Seventeen_ dealt with virtually identical subject matter and that was one of my favourite movies of the year.\r\n\r\nSo I'm just wrong I guess?\r\n\r\n_Final rating:★½: - Boring/disappointing. Avoid where possible._","url":"https://www.themoviedb.org/review/5a46240e9251411fab0942d8"}]
     * total_pages : 1
     * total_results : 2
     */

    private int id;
    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;
    private List<Comment> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Comment> getResults() {
        return results;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }

    public static class Comment {
        /**
         * id : 5a24615ac3a3680b9a0d882a
         * author : Innovator
         * content : This movie was ok (it wasn't boring nor was it very entertaining). At first I didn't understand the point it was trying to make. Was it you can shortcut your way to your dreams if you lie, cheat, and steal (as that was exactly what she did)? It wasn't about the pressures of class division (as a review I read states), as no one treated her as any differently regardless of her social status and any division she felt was just in her head. Her anxiety against her parents, the school, and the upper class of her school turned out to be non-issues, and when she got to where or what she wants, she just finds that the grass never gets greener for her once she was on the other side. In fact, she just becomes resented by those she abandons to get there and ultimately ends up longing for the side she left.

         If anything the movie was saying that those who are rich aren't as unexclusive as those without think, and they are just like everyone else. Just as the main protagonist was rebelling against her life to gain acceptance, so were the kids of the rich in that they are also just seeking acceptance. That doing anything to get your dreams won't lead to satisfaction as life on the other side isn't any better, you just end up hurting others by getting there that way, and in hindsight blinded you from what you already had.

         I guess the movie had something to say after all.

         ★★½ - Not dull, nor was it very entertaining.
         * url : https://www.themoviedb.org/review/5a24615ac3a3680b9a0d882a
         */

        private String id;
        private String author;
        private String content;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
