package br.com.delmano.zupchallenge.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.callback.ResponseCallback;
import br.com.delmano.zupchallenge.model.Movie;
import br.com.delmano.zupchallenge.rest.model.RestError;
import br.com.delmano.zupchallenge.view.DetailLineItem;
import teaspoon.annotations.OnBackground;
import teaspoon.annotations.OnUi;

/**
 * Created by pedro.oliveira on 13/02/17.
 */

@EActivity(R.layout.activity_details)
@OptionsMenu(R.menu.details)
public class MovieDetailsActivity extends MainActivity {

    @ViewById
    protected ImageView itemImage;

    @ViewById
    protected DetailLineItem release, time, director, actors, awards, plots;

    @ViewById
    protected TextView rating, votes;

    @Extra
    protected String movieId;
    private Movie movie;
    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        adjustItemMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void adjustItemMenu() {
        MenuItem exclude = menu.findItem(R.id.action_exclude);
        MenuItem save = menu.findItem(R.id.action_save);

        if (inMyMovies()) {
            exclude.setVisible(true);
            save.setVisible(false);
        } else {
            save.setVisible(true);
            exclude.setVisible(false);
        }
    }

    private boolean inMyMovies() {
        return Select.from(Movie.class).where(Condition.prop("imdb_id").eq(movieId)).count() > 0;
    }

    @AfterViews
    void afterViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showDialog();
        getDetails();
    }

    @OptionsItem(R.id.action_save)
    void saveItem() {
        movie.save();
        adjustItemMenu();
        Toast.makeText(this, "Filme adicionado a sua lista!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @OptionsItem(R.id.action_exclude)
    void excludeItem() {
        Select.from(Movie.class).where(Condition.prop("imdb_id").eq(movieId)).first().delete();
        adjustItemMenu();
        Toast.makeText(this, "Filme removido a sua lista!", Toast.LENGTH_SHORT).show();
    }

    @OnBackground
    protected void getDetails() {
        doRequest(rest.httpAPI().details(movieId), new ResponseCallback<Movie>() {
            @Override
            public void success(Movie item) {
                hideDialog();
                movie = item;
                adjustLayout();
            }

            @Override
            public void error(RestError restError) {
                hideDialog();
                if (inMyMovies())
                    loadLocalMovie();
                else
                    showError(restError);
            }
        });
    }

    private void loadLocalMovie() {
        movie = Select.from(Movie.class).where(Condition.prop("imdb_id").eq(movieId)).first();
        adjustLayout();
    }

    @OnUi
    protected void adjustLayout() {
        setTitle(movie.buildTitle());
        Glide.with(this).load(movie.getPoster()).fitCenter().into(itemImage);
        release.bind("Lançamento: ", movie.getReleased());
        time.bind("Duração: ", movie.getRuntime());
        director.bind("Diretor(es): ", movie.getDirector());
        actors.bind("Elenco: ", movie.getActors());
        awards.bind("Prêmio(s): ", movie.getAwards());
        plots.bind("Sinopse: ", movie.getPlot());
        rating.setText(movie.getImdbRating());
        votes.setText(movie.getImdbVotes());
    }
}
