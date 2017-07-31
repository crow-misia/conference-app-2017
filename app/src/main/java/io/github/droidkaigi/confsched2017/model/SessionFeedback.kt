package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class SessionFeedback @Setter constructor(

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("session_id")
    var sessionId: Int,

    @Column
    @SerializedName("session_title")
    var sessionTitle: String,

    @Column
    @SerializedName("relevancy")
    var relevancy: Int,

    @Column
    @SerializedName("as_expected")
    var asExpected: Int,

    @Column
    @SerializedName("difficulty")
    var difficulty: Int,

    @Column
    @SerializedName("knowledgeable")
    var knowledgeable: Int,

    @Column
    @SerializedName("comment")
    var comment: String? = null,

    @Column
    @SerializedName("is_submitted")
    var isSubmitted: Boolean = false
) {
    companion object {
        fun create(session: Session, relevancy: Int, asExpected: Int, difficulty: Int, knowledgeable: Int, comment: String?) =
                SessionFeedback(
                        sessionId = session.id,
                        sessionTitle = session.title,
                        relevancy = relevancy,
                        asExpected = asExpected,
                        difficulty = difficulty,
                        knowledgeable = knowledgeable,
                        comment = comment
                )
    }

    val isAllFilled: Boolean
        get() = sessionId > 0
                && relevancy > 0
                && asExpected > 0
                && difficulty > 0
                && knowledgeable > 0
}
