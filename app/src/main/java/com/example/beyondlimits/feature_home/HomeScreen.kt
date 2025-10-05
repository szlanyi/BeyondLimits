package com.example.beyondlimits.feature_home

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.components.TotalStatsFooter
import com.example.beyondlimits.ui.components.WeeklyProgressCard
import com.example.beyondlimits.ui.theme.BackgroundDark
import com.example.beyondlimits.util.Cycling
import com.example.beyondlimits.util.Route
import com.example.beyondlimits.util.Running
import com.example.beyondlimits.util.Swimming
import com.example.beyondlimits.util.Triathlon

@Composable
fun HomeScreen(
    vm: HomeViewModel = viewModel(),
    chosenTraining: (Route) -> Unit
) {
    val displayName by vm.displayName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF121212), // etwas dunkler oben
                        BackgroundDark,    // dein Farbton in der Mitte
                        Color(0xFF2A2A2A)  // leicht heller unten
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Welcome back, ${displayName ?: "Athlete"}!",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp
        )

        Text(
            text = "Push Beyond Your Limits",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            letterSpacing = 0.5.sp
        )



        Spacer(modifier = Modifier.height(24.dp))

        WeeklyProgressCard()

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Choose your training",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(20.dp))

        TrainingOptionGrid(onTrainingSelected = chosenTraining)
        Spacer(modifier = Modifier.height(32.dp))
        TotalStatsFooter()
    }
}

@Composable
fun TrainingOptionGrid(onTrainingSelected: (Route) -> Unit) {
    val items = listOf(
        TrainingItem("Running", R.drawable.running, Color(0xFF43A047), Running),
        TrainingItem("Cycling", R.drawable.tricycling, Color(0xFFFB8C00), Cycling),
        TrainingItem("Swimming", R.drawable.swimming, Color(0xFF039BE5), Swimming),
        TrainingItem("Triathlon", R.drawable.triathlon, Color(0xFF9C27B0), Triathlon)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        for (row in items.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { item ->
                    TrainingCard(
                        item = item,
                        onTrainingSelected = onTrainingSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TrainingCard(
    item: TrainingItem,
    onTrainingSelected: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        scale.animateTo(0.96f)
                        tryAwaitRelease()
                        scale.animateTo(1f)
                        onTrainingSelected(item.route)
                    }
                )
            }
            .aspectRatio(1.4f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTrainingSelected(item.route) }
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Hintergrundbild
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Verlauf-Overlay
        Box(
            Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
        )

        // Text oben drauf
        Text(
            text = item.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        )
    }
}



data class TrainingItem(
    val title: String,
    val iconRes: Int,
    val color: Color,
    val route: Route
)


