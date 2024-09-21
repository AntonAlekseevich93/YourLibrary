package bottom_sheets

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBottomSheet(
    successTitle: StringResource,
    dismissTitle: StringResource,
    infoText: StringResource,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()

    fun closeBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
        }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            windowInsets = WindowInsets(top = 58.dp),
            containerColor = ApplicationTheme.colors.cardBackgroundLight,
            onDismissRequest = {
                closeBottomSheet()
            },
            dragHandle = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle(color = ApplicationTheme.colors.hintColor)
                }
            },
            scrimColor = Color.Transparent
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(infoText),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Text(
                    text = stringResource(successTitle),
                    style = ApplicationTheme.typography.title3Bold,
                    color = Color(0xFFe65f61),
                    modifier = Modifier.padding(top = 26.dp, bottom = 4.dp).fillMaxWidth()
                        .clickable(
                            MutableInteractionSource(), null
                        ) {
                            closeBottomSheet()
                            onAccept()
                        },
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        closeBottomSheet()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ApplicationTheme.colors.screenColor.activeLinkColor,
                        disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, top = 24.dp, bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(dismissTitle),
                        style = ApplicationTheme.typography.title3Bold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}