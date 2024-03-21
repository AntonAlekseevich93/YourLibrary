import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.safety.Safelist
import main_models.BookError
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.ErrorType

class UrlParserInteractorImpl : UrlParserInteractor {

    override suspend fun parseBookUrl(url: String): BookItemResponse {
        return if (url.contains(LIVE_LIB_LINK)) {
            parseLiveLibUrl(url)
        } else if (url.contains(STORY_GRAPH_LINK)) {
            parseStoryGraphUrl(url)
        } else {
            BookItemResponse(
                bookItem = null,
                bookError = BookError(type = ErrorType.PARSE_ERROR_NOT_CORRECT_URL)
            )
        }
    }

    private suspend fun parseLiveLibUrl(url: String): BookItemResponse {
        val doc =
            Ksoup.parseGetRequest(url = url)
        val bookName = doc.title().substringBefore("—")
        val author = doc.body().select("a.bc-author__link").first()?.childNodes()
            ?.joinToString { it.toString() }
        var imgUrl = ""
        var isbn = ""
        var numbersOfPages = 0

        try {
            doc.select("meta").forEach {
                val keyProperty = it.attr("property")
                val value = it.attr("content")

                when (keyProperty) {
                    "og:image" -> imgUrl = value
                    "book:isbn" -> isbn = value
                }
            }
        } catch (_: Throwable) {
            //todo
        }

        try {
            doc.select("p").forEach {
                val pagesText = it.getElementsContainingText("Страниц:")
                if (pagesText.hasText()) {
                    pagesText.toString().substringAfter("Страниц: ").substringBefore("<")
                        .let { pages ->
                            pages.toIntOrNull()?.let { resultPages ->
                                numbersOfPages = resultPages
                            }
                        }
                    return@forEach
                }
            }
        } catch (_: Throwable) {
            //todo
        }

        return BookItemResponse(
            bookItem = createBookTesting(
                bookName = bookName,
                originalAuthorName = author,
                coverFromParsing = imgUrl,
                isbn = isbn,
                numbersOfPages = numbersOfPages
            ),
            bookError = null
        )
    }

    private suspend fun parseStoryGraphUrl(url: String): BookItemResponse {
        val doc =
            Ksoup.parseGetRequest(url = url)
        var imgUrl = ""
        var isbn = ""
        var bookName = ""
        var authorName = ""
        var description = ""
        var pages = 0

        try {
            doc.select("meta").forEach {
                val keyProperty = it.attr("property")
                val value = it.attr("content")

                when (keyProperty) {
                    "og:image" -> imgUrl = value
                    "og:title" -> {
                        bookName = value.substringBefore(" by")
                        authorName = value.substringAfter("by ")
                    }
                }
            }
        } catch (_: Throwable) {
            //todo
        }

        try {
            val descriptionHtml = doc.body()
                .select("script").select("script").first()
                .toString()
                .substringAfter("<strong>")
                .substringBefore("<\\/div></div>")

            val cleanDesc = Ksoup.clean(descriptionHtml, Safelist())
            description = cleanDesc.replace("\\/em", "").replace("\\/strong", "")
        } catch (_: Throwable) {
            //todo
        }

        try {
            doc.select("p").forEach {
                it.getElementsContainingOwnText("pages").firstOrNull().let { element ->
                    element?.text()?.let { text ->
                        text.substringAfter("\">").substringBefore(" pages").toIntOrNull()
                            ?.let { resultPages ->
                                pages = resultPages
                            }
                    }
                }
            }
        } catch (_: Throwable) {
            //todo
        }

        return BookItemResponse(
            bookItem = createBookTesting(
                bookName = bookName,
                originalAuthorName = authorName,
                coverFromParsing = imgUrl,
                isbn = isbn,
                description = description,
                numbersOfPages = pages
            ),
            bookError = null
        )
    }

    private suspend fun parseLitresUrl() {
        val doc =
            Ksoup.parseGetRequest(url = "https://www.litres.ru/book/stiven-king/billi-sammers-67303161/")
        val authorName =
            doc.body().select("div.BookAuthor-module__author__info_Kgg0a").select("span")
                .first()?.childNodes()?.first()
        val description =
            doc.body().select("div.BookCard-module__book__annotation_2ZIhf").first()
                ?.childNodes()
        val isbn = doc.body().select("div.CharacteristicsBlock-module__characteristic_2SKY6")
            .select("span").forEach {
                val key = it.childNodes().joinToString { it.toString() }

            }

        val headlines = doc.select("meta")

        val rating2 = doc.select("meta").forEach {
            val keyItemProp = it.attr("itemProp")
            val keyProperty = it.attr("property")
            val keyName = it.attr("name")
            var key = ""
            var value = it.attr("content")
            if (keyItemProp.isNotEmpty()) {
                key = keyItemProp
            } else if (keyProperty.isNotEmpty()) {
                key = keyProperty
            } else if (keyName.isNotEmpty()) {
                key = keyName
            }
            if (key.contains("og:title")) {
                value = value.substringAfter("«").substringBefore("»")
            }
            if (key.isNotEmpty() && value.isNotEmpty()) {
//                    println("key =[$key] | value=[$value]")
            }
        }
        println("Автор - $authorName\nОписание - $description\nISBN - $isbn\n rating2=$rating2\nheadlines = $headlines")
    }

    private fun createBookTesting(
        bookName: String? = null,
        originalAuthorName: String? = null,
        coverFromParsing: String? = null,
        isbn: String = "",
        description: String = "",
        numbersOfPages: Int? = null,
    ) = BookItemVo(
        id = "",
        originalAuthorId = "",
        modifiedAuthorId = null,
        statusId = "",
        shelfId = "",
        bookName = bookName ?: "Кто нашёл берет себе",
        originalAuthorName = originalAuthorName ?: "Кинг",
        modifiedAuthorName = null,
        description = description,
        coverUrl = "",
        coverUrlFromParsing = coverFromParsing
            ?: "https://cdn.book24.ru/v2/ASE000000000848034/COVER/cover13d__w820.jpg",
        numbersOfPages = numbersOfPages ?: 234,
        isbn = isbn,
        timestampOfCreating = 32,
        timestampOfUpdating = 324
    )

    companion object {
        private const val LITRES_LINK = "litres.ru"
        private const val STORY_GRAPH_LINK = "thestorygraph.com"
        private const val LIVE_LIB_LINK = "livelib.ru"
    }
}