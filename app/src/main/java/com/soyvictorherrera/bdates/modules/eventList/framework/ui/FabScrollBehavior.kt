package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

private const val SCROLL_UP_THRESHOLD = -1
private const val SCROLL_DOWN_THRESHOLD = 15

class FabScrollBehavior(
    private val fab: ExtendedFloatingActionButton,
) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) = Unit

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        when {
            dy < SCROLL_UP_THRESHOLD -> fab.show()
            dy > SCROLL_DOWN_THRESHOLD -> fab.hide()
        }
    }
}