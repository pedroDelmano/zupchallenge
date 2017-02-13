package br.com.delmano.zupchallenge.callback;

import br.com.delmano.zupchallenge.rest.model.RestError;

/**
 * Created by pedro.oliveira on 10/02/17.
 */

public interface ResponseCallback<T> {
    void success(T t);

    void error(RestError restError);
}
