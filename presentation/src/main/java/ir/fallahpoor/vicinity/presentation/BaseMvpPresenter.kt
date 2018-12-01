package ir.fallahpoor.vicinity.presentation

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseMvpPresenter<V : MvpView> : MvpBasePresenter<V>() {

    private val disposables = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun destroy() {
        super.destroy()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

}