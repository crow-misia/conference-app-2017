package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class Contributor @Setter constructor(

    @PrimaryKey(auto = false)
    @Column("name")
    @SerializedName(value = "login", alternate = arrayOf("name"))
    var name: String,

    @Column("avatar_url")
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @Column("html_url")
    @SerializedName("html_url")
    var htmlUrl: String? = null,

    @Column("contributions")
    @SerializedName("contributions")
    var contributions: Int = 0
)
