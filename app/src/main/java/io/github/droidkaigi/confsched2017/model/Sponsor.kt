package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class Sponsor @Setter constructor(

    @Column
    @SerializedName("image_url")
    var imageUrl: String,

    @Column
    @SerializedName("site_url")
    var url: String
)
