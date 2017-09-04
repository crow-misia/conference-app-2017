package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.support.annotation.DrawableRes
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.view.activity.SearchActivity
import io.github.droidkaigi.confsched2017.view.helper.Navigator

class SearchResultViewModel private constructor(private var text: String, val type: Type, private val session: Session, context: Context, private val navigator: Navigator,
                                                mySessionsRepository: MySessionsRepository) : BaseObservable(), ViewModel {

    val sessionTitle = session.title

    val speakerImageUrl = session.speaker?.imageUrl ?: ""

    val searchResultId = session.id * 10 + type.ordinal

    private val textAppearanceSpan = TextAppearanceSpan(context, R.style.SearchResultAppearance)

    val isMySession = mySessionsRepository.isExist(session.id)

    private val shouldEllipsis = type == Type.DESCRIPTION

    override fun destroy() {
        // Nothing to do
    }

    fun onItemClick(view: View) {
        navigator.navigateToSessionDetail(session, SearchActivity::class)
    }

    fun match(filterPattern: String) = text.toLowerCase().contains(filterPattern)

    val iconResId: Int
        @DrawableRes
        get() = type.iconResId

    fun getMatchedText(searchText: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (TextUtils.isEmpty(text)) {
            return builder
        }

        text = text.replace("\n", "  ")

        if (TextUtils.isEmpty(searchText)) {
            return builder.append(text)
        } else {
            val idx = text.toLowerCase().indexOf(searchText.toLowerCase())
            if (idx >= 0) {
                builder.append(text)
                builder.setSpan(
                        textAppearanceSpan,
                        idx,
                        idx + searchText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                if (idx > ELLIPSIZE_LIMIT_COUNT && shouldEllipsis) {
                    builder.delete(0, idx - ELLIPSIZE_LIMIT_COUNT)
                    builder.insert(0, ELLIPSIZE_TEXT)
                }

                return builder
            } else {
                return builder.append(text)
            }
        }
    }

    enum class Type constructor(@DrawableRes val iconResId: Int) {
        TITLE(R.drawable.ic_title_12_vector),
        DESCRIPTION(R.drawable.ic_description_12_vector),
        SPEAKER(R.drawable.ic_speaker_12_vector)
    }

    companion object {

        private const val ELLIPSIZE_TEXT = "..."

        private const val ELLIPSIZE_LIMIT_COUNT = 30


        internal fun createTitleType(session: Session, context: Context, navigator: Navigator,
                                     mySessionsRepository: MySessionsRepository): SearchResultViewModel {
            return SearchResultViewModel(session.title, Type.TITLE, session, context, navigator, mySessionsRepository)
        }

        internal fun createDescriptionType(session: Session, context: Context, navigator: Navigator,
                                           mySessionsRepository: MySessionsRepository): SearchResultViewModel {
            return SearchResultViewModel(session.desc ?: "", Type.DESCRIPTION, session, context, navigator, mySessionsRepository)
        }

        internal fun createSpeakerType(session: Session, context: Context, navigator: Navigator,
                                       mySessionsRepository: MySessionsRepository): SearchResultViewModel {
            return SearchResultViewModel(session.speaker?.name ?: "", Type.SPEAKER, session, context, navigator, mySessionsRepository)
        }
    }
}
