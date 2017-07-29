package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

import java.util.Date

@Table
class Session @Setter constructor(

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("id")
    var id: Int = 0,

    @Column(indexed = true)
    @SerializedName("title")
    var title: String,

    @Column
    @SerializedName("desc")
    var desc: String? = null,

    @Column(indexed = true)
    @SerializedName("speaker")
    var speaker: Speaker? = null,

    @Column
    @SerializedName("stime")
    var stime: Date,

    @Column
    @SerializedName("etime")
    var etime: Date,

    @Column
    @SerializedName("duration_min")
    var durationMin: Int,

    @Column
    @SerializedName("type")
    var type: String,

    @Column(indexed = true)
    @SerializedName("topic")
    var topic: Topic? = null,

    @Column(indexed = true)
    @SerializedName("room")
    var room: Room? = null,

    @Column
    @SerializedName("lang")
    var lang: String? = null,

    @Column
    @SerializedName("slide_url")
    var slideUrl: String? = null,

    @Column
    @SerializedName("movie_url")
    var movieUrl: String? = null,

    @Column
    @SerializedName("movie_dash_url")
    var movieDashUrl: String? = null,

    @Column
    @SerializedName("share_url")
    var shareUrl: String? = null
) {

    private enum class Type {
        CEREMONY, SESSION, BREAK, DINNER;

        internal fun matches(type: String): Boolean {
            return name.equals(type, ignoreCase = true)
        }
    }

    val isSession: Boolean
        get() = Type.SESSION.matches(type)

    val isCeremony: Boolean
        get() = Type.CEREMONY.matches(type)

    val isBreak: Boolean
        get() = Type.BREAK.matches(type)

    val isDinner: Boolean
        get() = Type.DINNER.matches(type)

    fun isLiveAt(whenever: Date): Boolean
            = stime.before(whenever) && etime.after(whenever)

    override fun equals(other: Any?): Boolean
            = other is Session && other.id == id || super.equals(other)

    override fun hashCode(): Int = id
}
