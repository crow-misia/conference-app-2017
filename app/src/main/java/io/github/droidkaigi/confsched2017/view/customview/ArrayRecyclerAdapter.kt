package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView

abstract class ArrayRecyclerAdapter<T, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(val context: Context, protected val list: MutableList<T> = arrayListOf()) : RecyclerView.Adapter<VH>() {

    @UiThread
    fun reset(items: Collection<T>) {
        clear()
        addAll(items)
        notifyDataSetChanged()
    }

    fun clear() = list.clear()

    fun addAll(items: Collection<T>) = list.addAll(items)

    fun getItem(position: Int): T = list[position]

    fun addItem(item: T) = list.add(item)

    fun addAll(position: Int, items: Collection<T>) = list.addAll(position, items)

    @UiThread
    fun addAllWithNotify(items: Collection<T>) {
        val position = itemCount
        addAll(items)
        notifyItemInserted(position)
    }

    override fun getItemCount() = list.size
}
