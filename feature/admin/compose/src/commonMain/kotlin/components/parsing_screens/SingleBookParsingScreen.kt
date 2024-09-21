package components.parsing_screens

import AdminViewModel
import ApplicationTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.AdminPanelAppBar
import components.ParsingBookDialog
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import elements.BookSelectorItem
import models.AdminEvents
import navigation.screen_components.SingleBookParsingScreenComponent
import org.jetbrains.compose.resources.stringResource
import text_fields.CommonTextField
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.single_book_parsing

@Composable
fun SingleBookParsingScreen(
    hazeState: HazeState,
    navigationComponent: SingleBookParsingScreenComponent,
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
    val scrollableState = rememberScrollState()
    var pastedUrl by remember { mutableStateOf("") }
    var showBookApproveDialog by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            AdminPanelAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.single_book_parsing),
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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = hazeModifier
                    .fillMaxSize()
                    .background(ApplicationTheme.colors.cardBackgroundDark)
                    .verticalScroll(scrollableState)
            ) {
                Column(
                    modifier = Modifier.padding(top = it.calculateTopPadding().plus(16.dp))
                        .fillMaxSize()
                ) {
                    CommonTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        labelText = "Ссылка на книгу",
                        textChangedListener = {
                            pastedUrl = it
                            if (pastedUrl.isEmpty()) {
                                viewModel.sendEvent(AdminEvents.OnClearParsedSingleBookData)
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                keyboardController?.hide()
                                viewModel.sendEvent(AdminEvents.OnParseSingleBook(pastedUrl))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = ApplicationTheme.colors.mainIconsColor
                                )
                            }
                        }
                    )

                    AnimatedVisibility(
                        visible = uiState.singleParsingBookProcess.value,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .background(ApplicationTheme.colors.cardBackgroundDark)
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = ApplicationTheme.colors.hintColor)
                        }
                    }

                    uiState.singleParsingBookResultMessage.value?.let {
                        Text(
                            text = it,
                            style = ApplicationTheme.typography.footnoteRegular,
                            color = ApplicationTheme.colors.mainTextColor,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }

                    uiState.singleParsingBook.value?.let {
                        BookSelectorItem(
                            bookItem = it,
                            modifier = Modifier.padding(end = 16.dp, start = 12.dp, top = 24.dp),
                            onClick = { selectedBook ->
                                viewModel.sendEvent(
                                    AdminEvents.OnBookSelected(selectedBook)
                                )
                            },
                            maxLinesBookName = 2,
                            maxLinesAuthorName = 1,
                            changeBookReadingStatus = {
                                showBookApproveDialog = !showBookApproveDialog
                            },
                            hazeModifier = hazeModifier
                        )
                    }
                }
            }

            if (showBookApproveDialog) {
                ParsingBookDialog(
                    onApprove = {
                        viewModel.sendEvent(AdminEvents.OnApproveParsedSingleBook)
                        showBookApproveDialog = !showBookApproveDialog
                    },
                    onDelete = {
                        viewModel.sendEvent(AdminEvents.OnClearParsedSingleBookData)
                        showBookApproveDialog = !showBookApproveDialog
                    },
                    dismiss = {
                        showBookApproveDialog = !showBookApproveDialog
                    }
                )
            }
        }
    }
}