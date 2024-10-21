package bottom_sheets

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
fun CommonBottomSheet(
    buttonTitle: StringResource,
    infoText: StringResource? = null,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable (() -> Unit)? = null,
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
            modifier = Modifier.padding(top = 58.dp),
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
                infoText?.let {
                    Text(
                        text = stringResource(it),
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                content?.invoke()

                Button(
                    onClick = {
                        closeBottomSheet()
                        onAccept()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ApplicationTheme.colors.screenColor.activeButtonColor,
                        disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(buttonTitle),
                        style = ApplicationTheme.typography.title3Bold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}