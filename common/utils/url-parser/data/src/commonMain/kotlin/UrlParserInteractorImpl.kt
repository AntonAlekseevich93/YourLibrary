import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import main_models.BookError
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.ErrorType

class UrlParserInteractorImpl : UrlParserInteractor {

    val bookItemForTesting = BookItemVo(
        id = 1,
        shelfId = 2,
        bookName = "Кто нашёл берет себе",
        authorName = "Стивен Кинг",
        description = "Четверо восьмиклассников, еще не знающих, что скоро станут друзьями, ведут обычную для подростков начала девяностых жизнь: учатся, дерутся, влюбляются, изучают карате по фильмам из видеосалонов, охотятся за джинсами-варенками или зарубежной фантастикой… Их случайно пролитая кровь разбудит того, кто спит под курганами.",
        coverUrl = "",
        coverUrlFromParsing = "https://cdn.book24.ru/v2/ASE000000000848034/COVER/cover13d__w820.jpg",
        numbersOfPages = 234,
        isbn = "978-5-17-148330-2"
    )

    override suspend fun parseBookUrl(url: String): BookItemResponse {
        return if (isUrlCorrect(url)) {
            BookItemResponse(
                bookItem = bookItemForTesting,
                bookError = null
            )

        } else {
            BookItemResponse(
                bookItem = null,
                bookError = BookError(type = ErrorType.PARSE_ERROR_NOT_CORRECT_URL)
            )
        }
    }

    private suspend fun parseLiveLibUrl() {
        val doc =
            Ksoup.parseGetRequest(url = "https://www.livelib.ru/book/1000439273-v-ozhidanii-varvarov-dzhozef-m-kutzee")
        val bookName = doc.title().substringBefore("—")
        val author = doc.body().select("a.bc-author__link").first()?.childNodes()
            ?.joinToString { it.toString() }

        doc.select("meta").forEach {
            val keyProperty = it.attr("property")

            var value = it.attr("content")

            if (keyProperty.contains("book:isbn")) {
                println("key=$keyProperty, value=$value")
            }
        }
    }

    private suspend fun parseLitresUrl() {

        val doc =
            Ksoup.parseGetRequest(url = "https://www.litres.ru/book/viktor-metos/zhena-ubiycy-63754131/")
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
    }

    private fun isUrlCorrect(url: String): Boolean {
        if (url.contains(LITRES_LINK)) return true
        if (url.contains(AMAZON_LINK)) return true
        if (url.contains(LIVE_LIB_LINK)) return true
        return false
    }

    companion object {
        private const val LITRES_LINK = "litres.ru"
        private const val AMAZON_LINK = "amazon.com"
        private const val LIVE_LIB_LINK = "livelib.ru"
    }
}