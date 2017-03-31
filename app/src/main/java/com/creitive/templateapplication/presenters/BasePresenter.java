package com.creitive.templateapplication.presenters;

import java.lang.ref.WeakReference;

/**
 * Used for general stuff that all presenters need
 */
public abstract class BasePresenter<V>  {

    protected WeakReference<V> mView;

    public BasePresenter() {}

    /**
     * bindView binds View to Presenter
     */
    public void bindView(V view) {
        mView = new WeakReference<>(view);
    }

    /**
     * unbindView unbinds View from Presenter
     */
    public void unbindView() {
        mView = null;
    }

    /**
     * Presenter uses getView to get attached View
     */
    public V getView() {
        if (mView == null) {
            return null;
        } else {
            return mView.get();
        }
    }
}
