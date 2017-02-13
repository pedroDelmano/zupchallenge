package br.com.delmano.zupchallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

public class Search implements Serializable {

    @SerializedName("totalResults")
    @Expose
    private int totalResults;


    @SerializedName("Response")
    @Expose
    private boolean response;

    @SerializedName("Search")
    @Expose
    private List<Movie> searchList = new ArrayList<>();

    public List<Movie> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Movie> searchList) {
        this.searchList = searchList;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
