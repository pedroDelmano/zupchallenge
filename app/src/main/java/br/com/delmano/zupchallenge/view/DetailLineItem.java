package br.com.delmano.zupchallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.activity.MainActivity;

/**
 * Created by pedro.oliveira on 13/02/17.
 */

@EViewGroup(R.layout.detail_line_item)
public class DetailLineItem extends LinearLayout {

    @ViewById
    protected TextView title, content;

    private MainActivity activity;

    public DetailLineItem(Context context) {
        super(context);
        activity = (MainActivity) context;
    }

    public DetailLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity) context;
    }

    public void bind(String titleText, String contentText) {
        title.setText(titleText);
        content.setText(contentText);
    }
}
