package io.github.droidkaigi.confsched2017.log

import timber.log.Timber

/**
 * @author KeithYokoma
 */
class OverlayLogTree(private val emitter: LogEmitter) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) = emitter.log(priority, tag, message)
}
