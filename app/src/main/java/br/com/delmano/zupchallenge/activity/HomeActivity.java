package br.com.delmano.zupchallenge.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.adapter.HomeFragmentPagerAdapter;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

@EActivity(R.layout.activity_home)
@OptionsMenu(R.menu.home)
public class HomeActivity extends MainActivity {

    @ViewById
    protected ListView myItems;

    @ViewById
    protected ViewPager viewPager;

    @ViewById
    protected PagerContainer pagerContainer;

    @Bean
    protected HomeFragmentPagerAdapter pagerAdapter;

    private boolean needsReload = false;

    @AfterViews
    void afterViews() {
        setTitle("Meus Filmes");
        adjustCarrousel();
    }

    private void adjustCarrousel() {
        new CoverFlow.Builder()
                .with(viewPager)
                .pagerMargin(0f)
                .scale(0.3f)
                .spaceSize(0f)
                .rotationY(0f)
                .build();

        viewPager.setAdapter(pagerAdapter);

        pagerContainer.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });

        pagerContainer.setOverlapEnabled(true);
//        viewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                Fragment fragment = pagerAdapter.getItem(0);
//                ViewCompat.setElevation(fragment.getView(), 8.0f);
//            }
//        });
    }

    @OptionsItem(R.id.action_search)
    void goToSearch() {
        needsReload = true;
        SearchActivity_.intent(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needsReload) {
            pagerAdapter.afterInject();
            pagerAdapter.notifyDataSetChanged();
            needsReload = false;
        }
    }

}
