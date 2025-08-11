package org.example.whiteboard.domain.repo

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.ColorScheme

interface SettingsRepo {

    suspend fun saveColorScheme(colorScheme: ColorScheme)

    fun getColorScheme(): Flow<ColorScheme>

    suspend fun savePreferredColors(colors: List<Color>, colorPaletteType: ColorPaletteType)

    fun getPreferredCanvasColors(): Flow<List<Color>>

    fun getPreferredStrokeColors(): Flow<List<Color>>

    fun getPreferredFillColors(): Flow<List<Color>>

    suspend fun saveAuthToken(token: String)

    fun getAuthToken(): Flow<String>

}