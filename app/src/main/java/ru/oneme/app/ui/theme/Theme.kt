package ru.oneme.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Azure,
    secondary = Azure,
    tertiary = StormGray,
    onPrimary = White,
    primaryContainer = GraniteBlack,
    onPrimaryContainer = DarkSilver,
    background = BlackOnyx,
    onBackground = SilverSand,
    surface = DarkCharcoal,
    onSurface = DecemberSky,
    surfaceVariant = BlackRock,
    onSurfaceVariant = Gray,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = Azure,
    tertiary = SilverFoil,
    onPrimary = White,
    primaryContainer = Aluminium,
    onPrimaryContainer = DecemberSky,
    background = White,
    onBackground = BlackPearl,
//    onSurfaceVariant = Gray,
    surface = Whisper,
    onSurface = ChineseBlack,
    error = Red,
    surfaceVariant = White
)

@Composable
fun MAXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}