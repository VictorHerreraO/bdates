package com.soyvictorherrera.bdates.modules.circles.framework.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.soyvictorherrera.bdates.core.compose.theme.BdatesTheme

class CircleListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BdatesTheme {
                    CircleListScreen(
                        circles = mockCircles,
                        activeCircleId = "1",
                        onAddCircleClick = { /* NO-OP for now */ },
                        onCircleClick = { /* NO-OP for now */ },
                        onMenuClick = { /* NO-OP for now */ }
                    )
                }
            }
        }
    }
}
