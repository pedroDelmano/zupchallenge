package br.com.delmano.zupchallenge.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import br.com.delmano.zupchallenge.model.Movie;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

@EBean
public class MovieListAdapter extends BaseAdapter {

    private List<Movie> myItems = new ArrayList<>();

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Movie getItem(int i) {
        return myItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public void setMyItems(List<Movie> myItems) {
        this.myItems.clear();
        this.myItems.addAll(myItems);
    }
}
