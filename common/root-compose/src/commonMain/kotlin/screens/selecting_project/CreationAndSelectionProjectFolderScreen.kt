package screens.selecting_project

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import main_models.path.PathInfoVo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.CreationAndSelectionProjectFolderScreen(
    pathInfoList: List<PathInfoVo>,
) {
    val showDirPicker = remember { mutableStateOf(false) }
    val isMainScreen = remember { mutableStateOf(true) }
    var selectedPath by remember { mutableStateOf("") }
    var isSelectedFolderProcess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(ApplicationTheme.colors.mainBackgroundColor)) {
        Row {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(ApplicationTheme.colors.cardBackgroundDark),
            ) {
                items(pathInfoList) { pathInfo ->
                    PathInfoBlock(
                        pathInfo = pathInfo,
                        selectedPathInfo = {
                            this@CreationAndSelectionProjectFolderScreen.sendEvent(
                                ProjectFoldersEvents.SelectPathInfo(pathInfo)
                            )
                        },
                        renamePath = { newName ->
                            this@CreationAndSelectionProjectFolderScreen.sendEvent(
                                ProjectFoldersEvents.RenamePath(pathInfo, newName)
                            )
                        },
                        restartApp = {
                            this@CreationAndSelectionProjectFolderScreen.sendEvent(
                                ProjectFoldersEvents.RestartApp
                            )
                        },
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp).weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Drawable.drawable_app_logo),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 64.dp, bottom = 10.dp)
                )

                Text(
                    text = "${Strings.version}: 0.1", //todo
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.textDescriptionColor,
                )

                Box {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AnimatedVisibility(
                            visible = isMainScreen.value,
                            enter = slideInHorizontally(
                                initialOffsetX = { -300 },
                                animationSpec = tween(durationMillis = 100),
                            ),
                            exit = slideOutHorizontally(
                                animationSpec = tween(durationMillis = 100),
                                targetOffsetX = { -300 }
                            )
                        ) {
                            Column {
                                BlockWithButton(
                                    title = Strings.select_path_for_save_data_title,
                                    description = Strings.select_path_for_save_data_description,
                                    modifier = Modifier.padding(top = 64.dp),
                                    buttonTitle = "${Strings.create} ",
                                    onClick = {
                                        isMainScreen.value = false
                                    }
                                )

                                BlockWithButton(
                                    title = Strings.open_folder_as_library,
                                    description = Strings.open_folder_as_library_description,
                                    buttonTitle = Strings.select,
                                    buttonColor = ApplicationTheme.colors.secondaryButtonColor,
                                    onClick = {
                                        isSelectedFolderProcess = true
                                        showDirPicker.value = true
                                    }
                                )
                            }
                        }
                    }

                    Column {
                        AnimatedVisibility(
                            visible = !isMainScreen.value,
                            enter = slideInHorizontally(initialOffsetX = { 2000 }),
                            exit = fadeOut() + slideOutHorizontally(
                                animationSpec = tween(durationMillis = 10),
                                targetOffsetX = { 2000 }
                            )
                        ) {
                            CreateLibraryBlock(
                                path = selectedPath,
                                showDirPicker = {
                                    showDirPicker.value = true
                                },
                                createVaultListener = { name ->
                                    this@CreationAndSelectionProjectFolderScreen.sendEvent(
                                        ProjectFoldersEvents.CreateFolder(selectedPath, name)
                                    )
                                },
                                onBackClick = {
                                    isMainScreen.value = true
                                },
                            )
                        }
                    }
                }
            }
        }

        DirectoryPicker(showDirPicker.value) { path ->
            if (path != null) {
                selectedPath = path
                if (isSelectedFolderProcess) {
                    isSelectedFolderProcess = false
                    this@CreationAndSelectionProjectFolderScreen.sendEvent(
                        ProjectFoldersEvents.SelectFolderEvent(
                            path
                        )
                    )
                }
            }
            showDirPicker.value = false
        }
    }
}

@Composable
fun BlockWithButton(
    title: String,
    description: String,
    buttonTitle: String,
    modifier: Modifier = Modifier,
    buttonColor: Color = ApplicationTheme.colors.primaryButtonColor,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f, fill = true))
        Column(modifier = Modifier.weight(3f)) {
            Text(
                text = title,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColorLight
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = description,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.textDescriptionColor,
            )
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Button(
                onClick = onClick,
                modifier = Modifier.sizeIn(maxHeight = 30.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                contentPadding = PaddingValues(horizontal = 10.dp),
            ) {
                Text(
                    text = buttonTitle,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColorLight
                )
            }
        }

        Spacer(Modifier.weight(1f, fill = true))
    }

    Row {
        Spacer(Modifier.weight(1f, fill = true))
        Divider(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).weight(4f),
            thickness = 1.dp,
            color = ApplicationTheme.colors.divider
        )
        Spacer(Modifier.weight(1f, fill = true))
    }
}
