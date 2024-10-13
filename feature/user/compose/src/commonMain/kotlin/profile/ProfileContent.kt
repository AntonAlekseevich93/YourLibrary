package profile

import androidx.compose.runtime.Composable
import models.UserUiState
import profile.elements.AccountMenu
import profile.elements.AvatarWithName
import profile.elements.UserBooksStatisticsInfo

@Composable
fun ProfileContent(
    uiState: UserUiState,
    onAdminPanelClick: () -> Unit,
    onSignOut: () -> Unit,
    onServiceDevelopmentClick: () -> Unit,
) {
    AvatarWithName(uiState.userInfo.value.name)
    UserBooksStatisticsInfo(
        userBooksStatistics = uiState.userBooksStatistics,
        userReviews = uiState.userReviews,
        onServiceDevelopmentClick = onServiceDevelopmentClick
    )
    AccountMenu(
        onAdminPanelClick = onAdminPanelClick,
        showAdminPanelMenu = uiState.showAdminPanel.value,
        onSignOut = onSignOut
    )
}