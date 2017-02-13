package br.com.delmano.zupchallenge.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.callback.ResponseCallback;
import br.com.delmano.zupchallenge.persistence.Prefs_;
import br.com.delmano.zupchallenge.rest.Rest;
import br.com.delmano.zupchallenge.rest.model.RestError;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import teaspoon.annotations.OnBackground;
import teaspoon.annotations.OnUi;

@EActivity
public abstract class MainActivity extends AppCompatActivity {

    @Bean
    protected Rest rest;

    @Pref
    protected Prefs_ prefs;

    @ViewById
    protected Toolbar toolbar;

    private ProgressDialog dialog;

    @OnUi
    public void showDialog() {
        if (dialog != null && dialog.isShowing())
            return;

        dialog = ProgressDialog.show(this, "",
                "Carregando . . .", true, true);
    }

    @OnUi
    public void showDialog(String message) {
        if (dialog != null && dialog.isShowing())
            return;

        dialog = ProgressDialog.show(this, "",
                message, true, true);
    }

    @OnUi
    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @AfterViews
    void baseAfterViews() {
        if (toolbar != null)
            setSupportActionBar(toolbar);
    }

    @OnBackground
    public <T> void doRequest(Call<T> request, ResponseCallback<T> callback) {
        try {
            Response<T> execute = request.execute();
            if (execute.isSuccessful() && execute.code() < 300 && execute.body() != null) {
                successRequest(callback, execute.body());
            } else {
                errorRequest(callback, execute.errorBody());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnUi
    public <T> void errorRequest(ResponseCallback<T> callback, ResponseBody responseBody) {
        RestError restError;

        try {
            restError = rest.errorConverter().convert(responseBody);
        } catch (IOException e) {
            restError = new RestError();
        }

        callback.error(restError);
    }

    @OnUi
    public <T> void successRequest(ResponseCallback<T> callback, T successObject) {
        callback.success(successObject);
    }

    public void showError(RestError error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aviso");
        builder.setMessage(error.getMessage());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
