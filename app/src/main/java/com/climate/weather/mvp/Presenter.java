package com.climate.weather.mvp;

/* All presenters must either implement this interface or extend BasePresenter */

public interface Presenter<T extends MvpView> {
    void attachView(T mvpView);
    void detachView();
    void onCreate();
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
