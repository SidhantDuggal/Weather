package com.climate.weather.mvp;

/**
 * Datasource that is to be inherited by any class acting as a model in MVP
 */

public class BaseDataSource<T extends Presenter> implements DataSource<T>{

    public T presenter;

    @Override
    public void attachPresenter(T presenter) {
        if (presenter == null){
            throw new NullPointerException("presenter can't be null");
        }
        this.presenter = presenter;
    }

    @Override
    public void detachPresenter() {
        this.presenter = null;
    }

    public boolean isPresenterAttached() {
        return presenter != null;
    }

    public T getPresenter() {
        return presenter;
    }

    public void checkPresenterAttached() {
        if (!isPresenterAttached()) throw new BaseDataSource.PresenterNotAttachedException();
    }

    public static class PresenterNotAttachedException extends RuntimeException {
        public PresenterNotAttachedException() {
            super("Please call DataSource.attachView(Presenter) before" +
                    " requesting for data from the DataSource");
        }
    }
}
