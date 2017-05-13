package com.climate.weather.mvp;

/**
 * Base datasource interface
 */

public interface DataSource<T extends Presenter> {
    void attachPresenter(T presenter);
    void detachPresenter();
}
