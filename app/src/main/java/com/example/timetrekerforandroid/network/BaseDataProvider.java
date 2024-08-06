package com.example.timetrekerforandroid.network;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaseDataProvider {
    protected Observable.Transformer schedulerTransformer;
    protected ApiService service = ServiceModule.getInstance().getService();
    protected ApiService downloadService = ServiceModule.getInstance().getDownloadService();

    public BaseDataProvider() {
        schedulerTransformer = o -> ((Observable) o)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulerTransformer;
    }
}
