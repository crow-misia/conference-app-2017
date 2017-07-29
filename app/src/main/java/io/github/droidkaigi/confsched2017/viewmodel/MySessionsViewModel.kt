package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.View

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.BR
import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class MySessionsViewModel @Inject internal constructor(
        private val navigator: Navigator, private val mySessionsRepository: MySessionsRepository,
        private val compositeDisposable: CompositeDisposable) : BaseObservable(), ViewModel {

    val mySessionViewModels: ObservableList<MySessionViewModel> = ObservableArrayList<MySessionViewModel>()

    @get:Bindable
    var emptyViewVisibility: Int = 0
        private set

    @get:Bindable
    var recyclerViewVisibility: Int = 0
        private set

    override fun destroy() = compositeDisposable.clear()

    private fun loadMySessions(): Single<List<MySession>> =
        mySessionsRepository.findAll()
                .map { it.sortedWith(Comparator { lhs, rhs -> lhs.session.stime.compareTo(rhs.session.stime) }).toList() }

    fun start(context: Context) =
        loadMySessions()
                .map { mySession -> convertToViewModel(context, mySession) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { this.renderMySessions(it) },
                        onError = { Timber.tag(TAG).e(it, "Failed to show my sessions.") }
                ).addTo(compositeDisposable)

    private fun convertToViewModel(context: Context, mySessions: List<MySession>): List<MySessionViewModel> =
        mySessions.map { mySession -> MySessionViewModel(context, navigator, mySession) }.toList()

    private fun renderMySessions(mySessionViewModels: List<MySessionViewModel>) {
        if (this.mySessionViewModels.size == mySessionViewModels.size) {
            return
        }
        this.mySessionViewModels.clear()
        this.mySessionViewModels.addAll(mySessionViewModels)
        this.emptyViewVisibility = if (this.mySessionViewModels.size > 0) View.GONE else View.VISIBLE
        this.recyclerViewVisibility = if (this.mySessionViewModels.size > 0) View.VISIBLE else View.GONE
        notifyPropertyChanged(BR.emptyViewVisibility)
        notifyPropertyChanged(BR.recyclerViewVisibility)
    }

    companion object {
        private val TAG = MySessionsViewModel::class.java.simpleName
    }
}
