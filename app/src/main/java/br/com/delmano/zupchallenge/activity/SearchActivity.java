package br.com.delmano.zupchallenge.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.adapter.MovieListAdapter;
import br.com.delmano.zupchallenge.callback.ResponseCallback;
import br.com.delmano.zupchallenge.model.Movie;
import br.com.delmano.zupchallenge.model.Search;
import br.com.delmano.zupchallenge.rest.model.RestError;
import br.com.delmano.zupchallenge.utils.KeyboardUtils;
import teaspoon.annotations.OnBackground;
import teaspoon.annotations.OnUi;

/**
 * Created by pedro.oliveira on 13/02/17.
 */

@EActivity(R.layout.activity_search)
public class SearchActivity extends MainActivity {

    @ViewById
    protected SearchView searchView;

    @ViewById
    protected ListView items;

    @Bean
    protected MovieListAdapter adapter;

    @AfterViews
    void afterViews() {
        items.setAdapter(adapter);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setQueryHint("Digite o título do filme");
        adjustSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty())
                    doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void adjustSearchView() {
        EditText editText = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        editText.setHintTextColor(getResources().getColor(android.R.color.white));
        editText.setTextColor(getResources().getColor(android.R.color.white));
        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    @ItemClick(R.id.items)
    void goToDetails(Movie movie) {
        MovieDetailsActivity_.intent(this).movieId(movie.getImdbId()).start();
    }


    @OnBackground
    protected void doSearch(String query) {
        showDialog("Buscando . . .");
        KeyboardUtils.hideKeyboard(searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text), this);
        doRequest(rest.httpAPI().search(query, 1), new ResponseCallback<Search>() {
            @Override
            public void success(Search search) {
                updateUi(search);
                hideDialog();
            }

            @Override
            public void error(RestError restError) {
                hideDialog();
                showError(restError);
            }
        });

    }

    @OnUi
    protected void updateUi(Search search) {
        KeyboardUtils.hideKeyboard(searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text), this);
        adapter.setMyItems(search.getSearchList());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
