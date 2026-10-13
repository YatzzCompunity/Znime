/*
 * Copyright (C) 2024 z-huang/InnerTune
 * Copyright (C) 2025 O﻿ute﻿rTu﻿ne Project
 *
 * SPDX-License-Identifier: GPL-3.0
 *
 * For any other attributions, refer to the git commit history
 */
package com.dd3boh.outertune.ui.theme

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import coil3.toUri
import com.dd3boh.outertune.playback.PlayerConnection
import com.dd3boh.outertune.utils.LocalArtworkPath
import com.dd3boh.outertune.utils.coilCoroutine
import com.google.material.color.dynamiccolor.DynamicScheme
import com.google.material.color.hct.Hct
import com.google.material.color.scheme.SchemeTonalSpot
import com.google.material.color.score.Score
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: support for custom accent
val DefaultThemeColor = Color(0xFF8B5CF6)

/**
 * ZMusic "Neon Pulse" design system.
 * Deep violet-black surfaces with violet -> cyan gradient accents, matching the ZMusic logo.
 */
val ZMusicViolet = Color(0xFF8B5CF6)
val ZMusicVioletDeep = Color(0xFF7C3AED)
val ZMusicCyan = Color(0xFF22D3EE)
val ZMusicCyanDeep = Color(0xFF0891B2)
val ZMusicPink = Color(0xFFF472B6)

val ZMusicBrandGradient = listOf(ZMusicViolet, ZMusicCyan)

fun zmusicDarkColorScheme() = darkColorScheme(
    primary = ZMusicViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E1A5E),
    onPrimaryContainer = Color(0xFFE4D6FF),
    secondary = ZMusicCyan,
    onSecondary = Color(0xFF04202A),
    secondaryContainer = Color(0xFF0E3A44),
    onSecondaryContainer = Color(0xFFBDEFFA),
    tertiary = ZMusicPink,
    onTertiary = Color(0xFF3A0A22),
    tertiaryContainer = Color(0xFF4A1230),
    onTertiaryContainer = Color(0xFFFBD3E8),
    background = Color(0xFF0B0B14),
    onBackground = Color(0xFFF2F0FA),
    surface = Color(0xFF12121C),
    onSurface = Color(0xFFF2F0FA),
    surfaceVariant = Color(0xFF1C1C28),
    onSurfaceVariant = Color(0xFFB9B4CC),
    surfaceTint = ZMusicViolet,
    inverseSurface = Color(0xFFF2F0FA),
    inverseOnSurface = Color(0xFF12121C),
    error = Color(0xFFFF8A8A),
    onError = Color(0xFF3A0A0A),
    errorContainer = Color(0xFF4A1414),
    onErrorContainer = Color(0xFFFFD6D6),
    outline = Color(0xFF3A3A4E),
    outlineVariant = Color(0xFF262633),
    scrim = Color.Black,
    surfaceBright = Color(0xFF1E1E2C),
    surfaceDim = Color(0xFF0B0B14),
    surfaceContainer = Color(0xFF16161F),
    surfaceContainerHigh = Color(0xFF1C1C28),
    surfaceContainerHighest = Color(0xFF23232F),
    surfaceContainerLow = Color(0xFF12121C),
    surfaceContainerLowest = Color(0xFF08080F),
)

fun zmusicLightColorScheme() = lightColorScheme(
    primary = ZMusicVioletDeep,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4D6FF),
    onPrimaryContainer = Color(0xFF2E1A5E),
    secondary = ZMusicCyanDeep,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBDEFFA),
    onSecondaryContainer = Color(0xFF0E3A44),
    tertiary = Color(0xFFDB2777),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFBD3E8),
    onTertiaryContainer = Color(0xFF4A1230),
    background = Color(0xFFFAFAFF),
    onBackground = Color(0xFF14141C),
    surface = Color.White,
    onSurface = Color(0xFF14141C),
    surfaceVariant = Color(0xFFF0EDF7),
    onSurfaceVariant = Color(0xFF4A4658),
    surfaceTint = ZMusicViolet,
    inverseSurface = Color(0xFF14141C),
    inverseOnSurface = Color(0xFFFAFAFF),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFFD5D0E2),
    outlineVariant = Color(0xFFE6E1F0),
    scrim = Color.Black,
    surfaceBright = Color.White,
    surfaceDim = Color(0xFFE8E5F0),
    surfaceContainer = Color(0xFFF4F2FA),
    surfaceContainerHigh = Color(0xFFEEEAF6),
    surfaceContainerHighest = Color(0xFFE8E4F2),
    surfaceContainerLow = Color(0xFFFAF8FF),
    surfaceContainerLowest = Color.White,
)

/** ZMusic signature shapes: extra-rounded, soft and modern. */
val ZMusicShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp),
)

@Composable
fun OuterTuneTheme(
    context: Context,
    playerConnection: PlayerConnection?,
    enableDynamicTheme: Boolean,
    highContrastCompat: Boolean,
    isSystemInDarkTheme: Boolean,
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    var themeColor by rememberSaveable(stateSaver = ColorSaver) {
        mutableStateOf(DefaultThemeColor)
    }

    LaunchedEffect(playerConnection, enableDynamicTheme, isSystemInDarkTheme) {
        val playerConnection = playerConnection
        if (!enableDynamicTheme || playerConnection == null) {
            themeColor = DefaultThemeColor
            return@LaunchedEffect
        }
        playerConnection.service.currentMediaMetadata.collectLatest { song ->
            coroutineScope.launch(coilCoroutine) {
                var ret = DefaultThemeColor
                if (song != null) {
                    val uri = (if (song.isLocal) song.localPath else song.thumbnailUrl)?.toUri()
                    if (uri != null) {
                        val model = if (uri.toString().startsWith("/storage/")) {
                            LocalArtworkPath(uri.toString(), 100, 100)
                        } else {
                            uri
                        }

                        val result = context.imageLoader.execute(
                            ImageRequest.Builder(context)
                                .data(model)
                                .allowHardware(false)
                                .build()
                        )

                        ret = result.image?.toBitmap()?.extractThemeColor() ?: DefaultThemeColor
                    }
                }
                themeColor = ret
            }
        }
    }

    val colorScheme = remember(darkTheme, pureBlack, themeColor) {
        if (themeColor == DefaultThemeColor) {
            // ZMusic signature look: hand-crafted Neon Pulse scheme instead of system dynamic colors
            val zmusicTheme = if (darkTheme) zmusicDarkColorScheme() else zmusicLightColorScheme()
            if (highContrastCompat) {
                zmusicTheme.copy(
                    secondaryContainer = zmusicTheme.surfaceContainerHigh,
                    onSecondaryContainer = zmusicTheme.secondary,
                )
            } else {
                zmusicTheme
            }.pureBlack(darkTheme && pureBlack)
        } else {
            SchemeTonalSpot(Hct.fromInt(themeColor.toArgb()), darkTheme, 0.0)
                .toColorScheme()
                .pureBlack(darkTheme && pureBlack)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = ZMusicShapes,
        typography = MaterialTheme.typography,
        content = content
    )
}

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation = Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val extractedColors = Palette.from(this)
        .maximumColorCount(16)
        .generate()
        .swatches
        .associate { it.rgb to it.population }

    val orderedColors = Score.score(extractedColors, 2, 0xff4285f4.toInt(), true)
        .sortedByDescending { Color(it).luminance() }

    return if (orderedColors.size >= 2)
        listOf(Color(orderedColors[0]), Color(orderedColors[1]))
    else
        listOf(Color(0xFF595959), Color(0xFF0D0D0D))
}

fun DynamicScheme.toColorScheme() = ColorScheme(
    primary = Color(primary),
    onPrimary = Color(onPrimary),
    primaryContainer = Color(primaryContainer),
    onPrimaryContainer = Color(onPrimaryContainer),
    inversePrimary = Color(inversePrimary),
    secondary = Color(secondary),
    onSecondary = Color(onSecondary),
    secondaryContainer = Color(secondaryContainer),
    onSecondaryContainer = Color(onSecondaryContainer),
    tertiary = Color(tertiary),
    onTertiary = Color(onTertiary),
    tertiaryContainer = Color(tertiaryContainer),
    onTertiaryContainer = Color(onTertiaryContainer),
    background = Color(background),
    onBackground = Color(onBackground),
    surface = Color(surface),
    onSurface = Color(onSurface),
    surfaceVariant = Color(surfaceVariant),
    onSurfaceVariant = Color(onSurfaceVariant),
    surfaceTint = Color(primary),
    inverseSurface = Color(inverseSurface),
    inverseOnSurface = Color(inverseOnSurface),
    error = Color(error),
    onError = Color(onError),
    errorContainer = Color(errorContainer),
    onErrorContainer = Color(onErrorContainer),
    outline = Color(outline),
    outlineVariant = Color(outlineVariant),
    scrim = Color(scrim),
    surfaceBright = Color(surfaceBright),
    surfaceDim = Color(surfaceDim),
    surfaceContainer = Color(surfaceContainer),
    surfaceContainerHigh = Color(surfaceContainerHigh),
    surfaceContainerHighest = Color(surfaceContainerHighest),
    surfaceContainerLow = Color(surfaceContainerLow),
    surfaceContainerLowest = Color(surfaceContainerLowest),
    primaryFixed = Color(primaryFixed),
    primaryFixedDim = Color(primaryFixedDim),
    onPrimaryFixed = Color(onPrimaryFixed),
    onPrimaryFixedVariant = Color(onPrimaryFixedVariant),
    secondaryFixed = Color(secondaryFixed),
    secondaryFixedDim = Color(secondaryFixedDim),
    onSecondaryFixed = Color(onSecondaryFixed),
    onSecondaryFixedVariant = Color(onSecondaryFixedVariant),
    tertiaryFixed = Color(tertiaryFixed),
    tertiaryFixedDim = Color(tertiaryFixedDim),
    onTertiaryFixed = Color(onTertiaryFixed),
    onTertiaryFixedVariant = Color(onTertiaryFixedVariant),
)

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
