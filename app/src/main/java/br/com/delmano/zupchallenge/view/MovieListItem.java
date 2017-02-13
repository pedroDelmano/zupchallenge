package br.com.delmano.zupchallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.activity.MainActivity;
import br.com.delmano.zupchallenge.model.Movie;

/**
 * Created by pedro.oliveira on 13/02/17.
 */

@EViewGroup(R.layout.movie_item)
public class MovieListItem extends LinearLayout {

    @ViewById
    protected TextView movieTitle;

    @ViewById
    protected ImageView moviePoster;

    private MainActivity activity;

    public MovieListItem(Context context) {
        super(context);
        activity = (MainActivity) context;
    }

    public MovieListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity) context;
    }

    public MovieListItem bind(Movie item) {
        return this;
    }
}
