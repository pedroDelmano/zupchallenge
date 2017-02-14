package br.com.delmano.zupchallenge.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;

import java.util.ArrayList;
import java.util.List;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.activity.HomeActivity;
import br.com.delmano.zupchallenge.activity.MovieDetailsActivity_;
import br.com.delmano.zupchallenge.model.Movie;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

@EBean
public class HomeFragmentPagerAdapter extends PagerAdapter {

    private List<Movie> myItems = new ArrayList<>();

    @RootContext
    protected HomeActivity activity;

    @SystemService
    protected LayoutInflater inflater;

    @AfterInject
    public void afterInject() {
        myItems = getMyItems();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.home_pager_item, container, false);
        final Movie movie = getItem(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.title);
        Glide.with(activity).load(movie.getPoster()).centerCrop().into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.needsReload();
                MovieDetailsActivity_.intent(activity).movieId(movie.getImdbId()).start();
            }
        });
        textView.setText(movie.buildTitle());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        int count = myItems.size();
        if (count == 0)
            activity.adjustHide();
        else
            activity.adjutVisible();
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    protected Movie getItem(int position) {
        return myItems.get(position);
    }

    public List<Movie> getMyItems() {
        return SugarRecord.listAll(Movie.class);
    }
}
