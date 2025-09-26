package com.asraven.jaranimationapp.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Converts a hex color string (e.g., "#RRGGBB" or "#AARRGGBB") to a Compose Color.
 *
 * @param hexColorString The hex color string.
 * @return The Compose Color if parsing is successful, otherwise Color.Unspecified.
 */
fun String?.toComposeColorOrUnspecified(): Color {
    if (this.isNullOrBlank()) {
        Log.d("tag", "Input hex color string is null or blank.")
        return Color.Unspecified
    }

    val sanitizedHex = if (this.startsWith("#")) this else "#$this"

    return try {
        // Attempt to parse to Int first for better error catching on format
        val colorInt = sanitizedHex.toColorInt()
        Color(colorInt)
    } catch (e: IllegalArgumentException) {
        Log.e("tag", "Failed to parse color string: $this")
        Color.Unspecified
    }
}
