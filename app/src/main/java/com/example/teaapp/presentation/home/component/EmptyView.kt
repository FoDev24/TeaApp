package com.example.teaapp.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.teaapp.R
import com.example.teaapp.presentation.common.rememberWindowSizeClass

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    windowSize: WindowSizeClass = rememberWindowSizeClass()
) {
    val iconSize = if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) 128.dp else 96.dp
    val spacing = if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) 24.dp else 16.dp

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = "No data",
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.height(spacing))
        Text(
            text = text,
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
