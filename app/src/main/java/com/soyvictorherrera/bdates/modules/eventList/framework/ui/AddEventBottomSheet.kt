package com.soyvictorherrera.bdates.modules.eventList.framework.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soyvictorherrera.bdates.core.compose.theme.setBdatesContent
import com.soyvictorherrera.bdates.modules.eventList.framework.ui.compose.AddEventSheetContent


class AddEventBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setBdatesContent {
            Column {
                AddEventSheetContent(
                    onBottomSheetDismiss = { dismiss() }
                )
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (view?.parent as ViewGroup).background =
                    ColorDrawable(Color.TRANSPARENT)
            }
        }
    }
}