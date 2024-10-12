package profile

import BaseEvent
import BaseEventScope
import androidx.compose.runtime.Composable
import models.UserUiState
import profile.elements.AccountMenu
import profile.elements.AvatarWithName
import profile.elements.UserBooksStatisticsInfo

@Composable
fun BaseEventScope<BaseEvent>.ProfileContent(
    uiState: UserUiState,
    onAdminPanelClick: () -> Unit,
    onSignOut: () -> Unit,
) {
    AvatarWithName(uiState.userInfo.value.name)
    UserBooksStatisticsInfo(
        userBooksStatistics = uiState.userBooksStatistics,
        userReviews = uiState.userReviews
    )
    AccountMenu(
        onAdminPanelClick = onAdminPanelClick,
        showAdminPanelMenu = uiState.showAdminPanel.value,
        onSignOut = onSignOut
    )
}