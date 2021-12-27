package com.soyvictorherrera.bdates.presentation.presenter

import java.lang.ref.WeakReference

abstract class BasePresenter<V : BasePresenter.View> {

    private var viewRef = WeakReference<V>(null)
    var view: V?
        get() = viewRef.get()
        set(value) {
            viewRef.clear()
            viewRef = WeakReference(value)
            value?.let(this::onViewSet)
        }

    abstract fun onViewSet(view: V)

    interface View

}
