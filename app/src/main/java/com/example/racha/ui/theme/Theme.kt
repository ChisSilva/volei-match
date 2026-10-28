package com.example.racha.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaEscuro = darkColorScheme(
    // Laranja do escudo, com texto escuro por cima (contraste bom)
    primary = Color(0xFFFF7A2E),
    onPrimary = Color(0xFF1C0F05),
    primaryContainer = Color(0xFF4A2410),
    onPrimaryContainer = Color(0xFFFFDCC8),

    // Azul-claro de apoio
    secondary = Color(0xFF9DB2E6),
    onSecondary = Color(0xFF0E1A3D),
    secondaryContainer = Color(0xFF26356A),
    onSecondaryContainer = Color(0xFFDDE5FF),

    // Dourado (usado nas reservas)
    tertiary = Color(0xFFF2C14E),
    onTertiary = Color(0xFF2E2100),
    tertiaryContainer = Color(0xFF4A3A10),
    onTertiaryContainer = Color(0xFFFFE9A8),

    // Fundo azul-marinho
    background = Color(0xFF0B1224),
    onBackground = Color(0xFFE6E9F2),
    surface = Color(0xFF0B1224),
    onSurface = Color(0xFFE6E9F2),
    surfaceVariant = Color(0xFF1C2848),
    onSurfaceVariant = Color(0xFFA9B3CC),

    // Cartões, do mais baixo ao mais alto
    surfaceContainerLowest = Color(0xFF080E1D),
    surfaceContainerLow = Color(0xFF101A32),
    surfaceContainer = Color(0xFF152040),
    surfaceContainerHigh = Color(0xFF1B2850),
    surfaceContainerHighest = Color(0xFF223060),

    outline = Color(0xFF5A6A99),
    outlineVariant = Color(0xFF2B3961),

    error = Color(0xFFFF7B7B),
    onError = Color(0xFF3B0A0A),
    errorContainer = Color(0xFF5C1A1A),
    onErrorContainer = Color(0xFFFFD9D9)
)

private val EsquemaClaro = lightColorScheme(
    // Laranja mais fechado, para o texto branco ficar legível
    primary = Color(0xFFD9480F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE2D1),
    onPrimaryContainer = Color(0xFF4A1A00),

    // Azul-marinho do escudo
    secondary = Color(0xFF1B2A4E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE4F7),
    onSecondaryContainer = Color(0xFF0E1A3D),

    tertiary = Color(0xFF9A6700),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFEFC2),
    onTertiaryContainer = Color(0xFF3F2A00),

    background = Color(0xFFF5F6FA),
    onBackground = Color(0xFF141A2E),
    surface = Color(0xFFF5F6FA),
    onSurface = Color(0xFF141A2E),
    surfaceVariant = Color(0xFFE3E7F2),
    onSurfaceVariant = Color(0xFF4F5A78),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAFBFD),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFEDF0F7),
    surfaceContainerHighest = Color(0xFFE6EAF4),

    outline = Color(0xFF8791AD),
    outlineVariant = Color(0xFFD3D9E8),

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun RachaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}