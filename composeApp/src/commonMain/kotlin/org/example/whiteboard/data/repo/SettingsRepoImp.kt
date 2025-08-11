package org.example.whiteboard.data.repo

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.example.whiteboard.data.util.Constants
import org.example.whiteboard.domain.model.ColorPaletteType
import org.example.whiteboard.domain.model.ColorScheme
import org.example.whiteboard.domain.repo.SettingsRepo
import org.example.whiteboard.presentation.theme.defaultCanvasColors
import org.example.whiteboard.presentation.theme.defaultDrawingColors

class SettingsRepoImp(
    private val pref: DataStore<Preferences>
) : SettingsRepo {

    companion object {
        private val COLOR_SCHEME_KEY = stringPreferencesKey(Constants.COLOR_SCHEME_KEY)
        private val PREFERRED_STROKE_COLOR_KEY =
            stringPreferencesKey(Constants.PREFERRED_STROKE_COLOR_KEY)
        private val PREFERRED_FILL_COLOR_KEY =
            stringPreferencesKey(Constants.PREFERRED_FILL_COLOR_KEY)
        private val PREFERRED_CANVAS_COLOR_KEY =
            stringPreferencesKey(Constants.PREFERRED_CANVAS_COLOR_KEY)
        private val AUTH_TOKEN_KEY =
            stringPreferencesKey(Constants.AUTH_TOKEN_KEY)
    }

    override suspend fun saveColorScheme(colorScheme: ColorScheme) {
        pref.edit {
            it[COLOR_SCHEME_KEY] = colorScheme.name
        }
    }

    override fun getColorScheme(): Flow<ColorScheme> {
        return pref.data.map {
            val scheme = it[COLOR_SCHEME_KEY] ?: ColorScheme.SYSTEM_DEFAULT.name
            ColorScheme.valueOf(scheme)
        }
    }

    override suspend fun savePreferredColors(
        colors: List<Color>,
        colorPaletteType: ColorPaletteType
    ) {

        val colorsString = colors.map { it.toArgb() }.joinToString()

        val key = when (colorPaletteType) {
            ColorPaletteType.STROKE -> PREFERRED_STROKE_COLOR_KEY
            ColorPaletteType.FILL -> PREFERRED_FILL_COLOR_KEY
            ColorPaletteType.CANVAS -> PREFERRED_CANVAS_COLOR_KEY
        }

        pref.edit {
            it[key] = colorsString
        }

    }

    override fun getPreferredCanvasColors(): Flow<List<Color>> {
        return pref.data.map { preferences ->
            val colorsString = preferences[PREFERRED_CANVAS_COLOR_KEY]
            val colorList = colorsString?.toColors() ?: defaultCanvasColors
            colorList
        }
    }

    override fun getPreferredStrokeColors(): Flow<List<Color>> {
        return pref.data.map { preferences ->
            val colorsString = preferences[PREFERRED_STROKE_COLOR_KEY]
            val colorList = colorsString?.toColors() ?: defaultDrawingColors
            colorList
        }
    }

    override fun getPreferredFillColors(): Flow<List<Color>> {
        return pref.data.map { preferences ->
            val colorsString = preferences[PREFERRED_FILL_COLOR_KEY]
            val colorList = colorsString?.toColors() ?: defaultDrawingColors
            colorList
        }
    }

    override suspend fun saveAuthToken(token: String) {
        pref.edit {
            it[AUTH_TOKEN_KEY] = token
        }
    }

    override fun getAuthToken(): Flow<String> {
        return pref.data.map {
            it[AUTH_TOKEN_KEY] ?: ""
        }
    }


    private fun String.toColors(): List<Color> {
        return this.split(", ").map { it.toInt() }.map { Color(it) }
    }
}