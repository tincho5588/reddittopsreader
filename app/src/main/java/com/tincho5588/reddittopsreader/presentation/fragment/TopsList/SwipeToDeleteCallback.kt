package com.tincho5588.reddittopsreader.presentation.fragment.TopsList

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tincho5588.reddittopsreader.R

class SwipeToDeleteCallback(
    val context: Context,
    val callback: (adapterPosition: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val icon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_black)!!

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback(viewHolder.bindingAdapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView

        // Determine icon position
        val iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
        val iconRight: Int = itemView.right - iconMargin
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        // Draw it on the canvas
        icon.draw(c)
    }
}