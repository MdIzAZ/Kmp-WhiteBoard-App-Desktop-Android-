package org.example.whiteboard.presentation.dashboard

import org.example.whiteboard.domain.model.Whiteboard

data class DashboardState(
    val whiteboards: List<Whiteboard> = emptyList(),
    val isLoading: Boolean = false,
    val toastMessage: String? = null
)
