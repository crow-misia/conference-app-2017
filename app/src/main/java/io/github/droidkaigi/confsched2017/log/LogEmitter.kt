package io.github.droidkaigi.confsched2017.log

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * @author KeithYokoma
 */
@Singleton
class LogEmitter @Inject constructor() {
    private val subject = PublishSubject.create<OverlayLog>()

    fun log(priority: Int, tag: String?, message: String?) = subject.onNext(OverlayLog(priority, tag, message))

    fun listen(): Observable<OverlayLog> = subject
}
