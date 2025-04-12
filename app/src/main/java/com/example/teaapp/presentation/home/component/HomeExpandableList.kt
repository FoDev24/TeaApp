package com.example.teaapp.presentation.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.teaapp.domain.model.Area
import com.example.teaapp.domain.model.Competition

@Composable
fun HomeExpandableList(
    modifier: Modifier = Modifier,
    areas: List<Area>,
    competitionsByArea: Map<Int, List<Competition>>,
    onAreaExpanded: (Int) -> Unit,
    onCompetitionClick: (Competition) -> Unit,
) {
    val expandedAreaIds = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(areas) { area ->
            val isExpanded = expandedAreaIds[area.id] ?: false

            Column(modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .clickable {
                    val currentlyExpanded = expandedAreaIds[area.id] ?: false
                    expandedAreaIds[area.id] = !currentlyExpanded
                    if (!currentlyExpanded) {
                        onAreaExpanded(area.id)
                    }
                }) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${area.name} (${area.countryCode ?: "N/A"})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(visible = isExpanded) {
                    val competitions = (competitionsByArea[area.id] ?: emptyList())
                        .filter { it.area.id == area.id }

                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        if (competitions.isEmpty()) {
                            Text(
                                text = "No competitions available",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else {
                            competitions.forEach { competition ->
                                CompetitionCard(competition = competition, onClick = {
                                    onCompetitionClick(competition)
                                })
                            }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
