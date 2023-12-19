import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest

class UrlParserInteractorImpl : UrlParserInteractor {

    override suspend fun parseUrl(url: String) {
        TODO("Not yet implemented")
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

}