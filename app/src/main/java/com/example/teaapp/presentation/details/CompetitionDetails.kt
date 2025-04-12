package com.example.teaapp.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.teaapp.domain.model.Competition
import com.example.teaapp.presentation.common.AsyncSvgImage
import com.example.teaapp.presentation.common.rememberWindowSizeClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionDetails(
    modifier: Modifier = Modifier,
    competition: Competition,
    navBack: () -> Unit
) {
    val windowSize = rememberWindowSizeClass()
    val scrollState = rememberScrollState()

    val padding = if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) 32.dp else 16.dp
    val emblemSize = if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) 140.dp else 100.dp
    val spacing = if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) 32.dp else 16.dp

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Competition details")
                }
            },
            navigationIcon = {
                IconButton(onClick = navBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            competition.emblem?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(emblemSize)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Inside
                )
            }

            Spacer(modifier = Modifier.height(spacing))
            Text(
                text = competition.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = competition.type,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = competition.plan,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(spacing))

            Text("Region", style = MaterialTheme.typography.titleMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                competition.area.flag?.let {
                    AsyncSvgImage(model = it, context = LocalContext.current)
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(competition.area.name ?: "Unknown", style = MaterialTheme.typography.bodyLarge)
                    Text(competition.area.countryCode ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(spacing))

            Text("Current Season", style = MaterialTheme.typography.titleMedium)
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text("Start: ${competition.currentSeason?.startDate ?: "N/A"}")
                Text("End: ${competition.currentSeason?.endDate ?: "N/A"}")
                Text("Matchday: ${competition.currentSeason?.currentMatchday ?: "N/A"}")
            }

            Spacer(modifier = Modifier.height(spacing))

            Text("Seasons available: ${competition.numberOfAvailableSeasons}")
            Text(
                "Last Updated: ${competition.lastUpdated}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
