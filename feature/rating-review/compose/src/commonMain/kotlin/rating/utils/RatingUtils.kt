package rating.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun GetRatingColor(rating: Double): Color =
    when (rating) {
        in 1.0..2.9 -> {
            Color(0xFF9d0208)
        }

        in 3.0..3.9 -> {
            Color(0xFFdc2f02)
        }

        in 4.0..4.5 -> {
            Color(0xFFe85d04)
        }

        else -> {
            Color(0xFF80b918)
        }
    }