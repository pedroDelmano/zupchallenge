package br.com.delmano.zupchallenge.application;

import android.app.Application;

import com.orm.SugarContext;

import org.androidannotations.annotations.EApplication;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

@EApplication
public class ZupChallengeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(getApplicationContext());
    }
}
