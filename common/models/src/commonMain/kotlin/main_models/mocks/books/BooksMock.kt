package main_models.mocks.books

import main_models.BookVo
import main_models.ReadingStatus
import main_models.books.LANG
import main_models.service_development.BookWithServiceDevelopment
import main_models.service_development.UserServiceDevelopmentBookVo

object BooksMock {
    fun getListUserBooks(size: Int): List<BookVo> {
        val items = mutableListOf<BookVo>()
        repeat(size) {
            items.add(createUserBook(false))
        }
        return items
    }

    fun getListBookWithServiceDevelopment(
        size: Int,
        isApproved: Boolean,
        isModerated: Boolean,
        reasonForRefusal: String = ""
    ): List<BookWithServiceDevelopment> {
        val items = mutableListOf<BookWithServiceDevelopment>()
        repeat(size) {
            val book = createUserBook(false)
            val serviceDevelopment =
                createServiceDevelopment(isApproved, isModerated, reasonForRefusal)
            items.add(
                BookWithServiceDevelopment(
                    book = book,
                    serviceDevelopmentBookVo = serviceDevelopment
                )
            )
        }
        return items
    }

    private fun createServiceDevelopment(
        isApproved: Boolean,
        isModerated: Boolean,
        reasonForRefusal: String
    ): UserServiceDevelopmentBookVo {
        return UserServiceDevelopmentBookVo(
            id = -1,
            userId = -1,
            userBookId = "bookId",
            userAuthorId = "authorId",
            originalAuthorId = "authorId",
            isApproved = isApproved,
            isModerated = isModerated,
            updatedByModeratorId = -1,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            updatedByDeviceId = "deviceId",
            reasonForRefusal = reasonForRefusal,
        )
    }

    private fun createUserBook(isServiceDevelopment: Boolean): BookVo {
        return BookVo(
            bookId = "bookId",
            serverId = -1,
            localId = null,
            originalAuthorId = "authorId",
            bookName = "Тестовая книга",
            bookNameUppercase = "ТЕСТОВАЯ КНИГА",
            originalAuthorName = "Тестовый Автор",
            description = "Описание книги",
            userCoverUrl = "https://cdn1.ozone.ru/s3/multimedia-w/6010125140.jpg",
            pageCount = 232,
            isbn = "123-123-123",
            readingStatus = ReadingStatus.READING,
            ageRestrictions = "18+",
            bookGenreId = 2,
            startDateInString = "",
            endDateInString = "",
            startDateInMillis = 0,
            endDateInMillis = 0,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isRussian = null,
            imageName = "",
            authorIsCreatedManually = false,
            isLoadedToServer = false,
            bookIsCreatedManually = false,
            imageFolderId = null,
            ratingValue = 4.0,
            ratingCount = 15,
            reviewCount = 12,
            ratingSum = 25,
            isServiceDevelopmentBook = isServiceDevelopment,
            originalMainBookId = "bookId",
            lang = LANG.RUSSIAN.value,
            publicationYear = "2023",
            userId = 4,
            authorFirstName = "Тестовый",
            authorLastName = "Автор",
            authorMiddleName = "",
        ).apply { remoteImageLink = "https://cdn1.ozone.ru/s3/multimedia-w/6010125140.jpg" }
    }

}