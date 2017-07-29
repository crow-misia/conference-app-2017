package io.github.droidkaigi.confsched2017.model

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.google.gson.annotations.SerializedName

@Table
class Topic @Setter constructor(

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("id")
    var id: Int,

    @Column(indexed = true)
    @SerializedName("name")
    var name: String,

    @Column
    @SerializedName("other")
    var other: String? = null
)
