package io.github.droidkaigi.confsched2017.model

import com.google.gson.annotations.SerializedName

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class MySession @Setter constructor(

    @PrimaryKey
    @Column(indexed = true)
    @SerializedName("id")
    var id: Int = 0,

    @Column(indexed = true, unique = true)
    @SerializedName("session")
    var session: Session
) {

    override fun equals(other: Any?): Boolean
            = other is MySession && other.id == id || super.equals(other)

    override fun hashCode(): Int = id
}
