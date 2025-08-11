package org.example.whiteboard.presentation.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.whiteboard.domain.model.ColorScheme
import org.example.whiteboard.domain.repo.SettingsRepo


class SettingsViewModel(
    private val settingsRepo: SettingsRepo
): ViewModel() {

    val currentColorScheme : StateFlow<ColorScheme> = settingsRepo.getColorScheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ColorScheme.SYSTEM_DEFAULT
        )


    fun saveColorScheme(colorScheme: ColorScheme) {
        viewModelScope.launch {
            try {
                settingsRepo.saveColorScheme(colorScheme)
            } catch (e: Exception) {

            }
        }

    }

    val token = settingsRepo.getAuthToken()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )



}