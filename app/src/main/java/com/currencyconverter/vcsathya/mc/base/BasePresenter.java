package com.currencyconverter.vcsathya.mc.base;

public class BasePresenter<T> implements BasePresenterInterface<T> {

    protected T view;

    @Override
    public void onViewActive(T view) {
        this.view = view;
    }

}
