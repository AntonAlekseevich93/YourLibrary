package moderations_books_covers_screen

import AdminViewModel
import ApplicationTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.AdminPanelAppBar
import components.modarations_books_screen.elements.BookCover
import containters.CenterColumnContainerMaxSize
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import models.AdminEvents
import navigation.screen_components.ModerationBooksCoversScreenComponent
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.moderation_books_title

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModerationBooksCoversScreen(
    hazeState: HazeState,
    navigationComponent: ModerationBooksCoversScreenComponent,
) {
    val viewModel = remember { Inject.instance<AdminViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    var hazeModifier: Modifier = Modifier
    if (uiState.isHazeBlurEnabled.value) {
        hazeModifier = hazeModifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    }

    LaunchedEffect(uiState.moderationBookState.booksForModeration.size) {
        if (uiState.moderationBookState.booksForModeration.size == 0) {
            navigationComponent.onBack()
        }
    }

    val scrollableState = rememberScrollState()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.moderation_books_title),
                onClose = {
                    navigationComponent.onCloseScreen()
                },
                onBack = {
                    navigationComponent.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = hazeModifier.fillMaxSize()
                .background(ApplicationTheme.colors.cardBackgroundDark)
                .verticalScroll(scrollableState)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = it.calculateTopPadding().plus(16.dp),
                        bottom = 16.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                uiState.moderationBookState.booksForModeration.forEach { book ->
                    Column(
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BookCover(
                            coverUrl = book.rawCoverUrl.orEmpty(),
                            modifier = Modifier
                                .sizeIn(
                                    minHeight = 220.dp,
                                    minWidth = 140.dp,
                                    maxHeight = 220.dp,
                                    maxWidth = 140.dp
                                ),
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                HapticFeedbackType.LongPress
                                viewModel.sendEvent(AdminEvents.OnDiscardBookCovers(book))
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.sendEvent(AdminEvents.ApproveAllBooks)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ApplicationTheme.colors.screenColor.activeButtonColor,
                    disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        top = 24.dp,
                        bottom = it.calculateBottomPadding().plus(16.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = "Одобрить все книги",
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }

        AnimatedVisibility(
            uiState.isLoading,
            enter = scaleIn(animationSpec = tween()),
            exit = scaleOut()
        ) {
            CenterColumnContainerMaxSize {
                CircularProgressIndicator(
                    color = ApplicationTheme.colors.screenColor.activeButtonColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

