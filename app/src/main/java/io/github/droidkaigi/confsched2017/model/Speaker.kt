package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

import io.github.droidkaigi.confsched2017.BuildConfig
import timber.log.Timber

@Table
class Speaker @Setter constructor(

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("id")
    var id: Int,

    @Column(indexed = true)
    @SerializedName("name")
    var name: String,

    @Column
    @SerializedName("image_url")
    var imageUrl: String? = null,

    @Column
    @SerializedName("twitter_name")
    var twitterName: String? = null,

    @Column
    @SerializedName("github_name")
    var githubName: String? = null
) {

    val adjustedImageUrl: String?
        get() = imageUrl?.let {
            when {
                it.startsWith("http://") -> it
                it.startsWith("https://") -> it
                it.startsWith("/") -> BuildConfig.STATIC_ROOT + it
                else -> null.apply {
                    Timber.tag(TAG).e("Invalid image url: %s", it)
                }
            }
        }

    private companion object {

        val TAG = Speaker::class.java.simpleName
    }
}
