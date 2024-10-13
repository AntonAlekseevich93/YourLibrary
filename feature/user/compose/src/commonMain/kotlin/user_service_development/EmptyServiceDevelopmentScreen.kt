package user_service_development

import ApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.user_service_development_empty_screen_description
import yourlibrary.common.resources.generated.resources.user_service_development_empty_screen_title

@Composable
internal fun EmptyServiceDevelopmentScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.user_service_development_empty_screen_title),
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
        )

        Text(
            text = stringResource(Res.string.user_service_development_empty_screen_description),
            style = ApplicationTheme.typography.headlineRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}