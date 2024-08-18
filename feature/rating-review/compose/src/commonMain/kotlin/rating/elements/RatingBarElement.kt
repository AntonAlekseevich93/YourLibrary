package rating.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBarElement(
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    rating: Int = 0,
    selectedRatingListener: ((selectedRating: Int) -> Unit)? = null,
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            if (index < rating) {
                Icon(imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color(0xFFfaa307),
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .clickable(enabled = selectedRatingListener != null) {
                            selectedRatingListener?.invoke(index+1)
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = null,
                    tint = Color(0xFF6e6e6e),
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .clickable(enabled = selectedRatingListener != null) {
                            selectedRatingListener?.invoke(index+1)
                        }
                )
            }
        }
    }
}
