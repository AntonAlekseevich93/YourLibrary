package models

import platform.Platform
import platform.isDesktop

class BookItemCardConfig(
    private val platform: Platform,
    val maxItemsInHorizontalShelf: Int = 10,
    val width: Int = if (platform.isDesktop()) 160 else 160,
    val height: Int = if (platform.isDesktop()) 225 else 230
)