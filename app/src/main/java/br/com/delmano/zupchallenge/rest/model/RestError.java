package br.com.delmano.zupchallenge.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

public class RestError implements Serializable {

    @SerializedName("Response")
    @Expose
    private boolean response;


    @SerializedName("Error")
    @Expose
    private String message = " Houve um problema. Tente mais tarde";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
