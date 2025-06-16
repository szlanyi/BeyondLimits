import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.home.HomeViewModel
import com.example.beyondlimits.ui.theme.BackgroundDark
import com.example.beyondlimits.ui.theme.TextWhite
import com.example.beyondlimits.util.Cycling
import com.example.beyondlimits.util.Route
import com.example.beyondlimits.util.Running
import com.example.beyondlimits.util.Swimming
import com.example.beyondlimits.util.Triathlon
import com.example.beyondlimits.util.bounceClick
import com.example.beyondlimits.util.shakeClickEffect


@Composable
fun HomeScreen(vm: HomeViewModel = viewModel(), chosedTraning: (Route) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(BackgroundDark),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Train Hard! Go Beyond!",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(80.dp))

        val trainings = listOf("Running", "Cycling", "Swimming", "Triathlon")
        trainings.forEach { training ->
            TraningButton(text = training, onButtonClick = { route -> chosedTraning(route) })
        }
    }
}

@Composable
fun TraningButton(text: String, onButtonClick: (Route) -> Unit) {

    Box(
        modifier = Modifier
            .shakeClickEffect()
            .fillMaxWidth()
            .height(140.dp)
            .padding(end = 15.dp, bottom = 15.dp)
            .clip(CutCornerShape(bottomEnd = 140.dp))
            .clickable(enabled = true, onClick = {
                val route = when (text) {
                    "Running" -> Running
                    "Cycling" -> Cycling
                    "Swimming" -> Swimming
                    "Triathlon" -> Triathlon
                    else -> Running
                }
                onButtonClick(route)
            })
    ) {
        val image = when (text) {
            "Running" -> R.drawable.running
            "Cycling" -> R.drawable.tricycling
            "Swimming" -> R.drawable.swimming
            "Triathlon" -> R.drawable.triathlon
            else -> R.drawable.running
        }
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.7f,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = TextWhite,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
