package main_models.books

enum class LANG(val value: String, val translatedName: String) {
    RUSSIAN("RU", "Русский"),
    ENGLISH("EN", "Английский"),
    SPANISH("ES", "Испанский"),
    FRENCH("FR", "Французский"),
    GERMAN("DE", "Немецкий"),
    UKRAINIAN("UK", "Украинский"),
    CHINESE("ZH", "Китайский"),
    KOREAN("KO", "Корейский"),
    JAPANESE("JA", "Японский"),
    ITALIAN("IT", "Итальянский"),
    PORTUGUESE("PT", "Португальский"),
    ARABIC("AR", "Арабский"),
    TURKISH("TR", "Турецкий"),
    DUTCH("NL", "Нидерландский"),
    POLISH("PL", "Польский"),
    SWEDISH("SV", "Шведский"),
    DANISH("DA", "Датский"),
    FINNISH("FI", "Финский"),
    NORWEGIAN("NO", "Норвежский"),
    GREEK("EL", "Греческий"),
    CZECH("CS", "Чешский"),
    HEBREW("HE", "Иврит"),
    THAI("TH", "Тайский"),
    INDONESIAN("ID", "Индонезийский"),
    VIETNAMESE("VI", "Вьетнамский"),
    HINDI("HI", "Хинди"),
    MALAY("MS", "Малайский"),
    TAMIL("TA", "Тамильский"),
    TELUGU("TE", "Телугу"),
    BENGALI("BN", "Бенгальский"),
    HUNGARIAN("HU", "Венгерский"),
    ROMANIAN("RO", "Румынский"),
    UNKNOWN("", "Неопределено")
}

fun String.toLang(): LANG {
    return when (this) {
        "RU" -> LANG.RUSSIAN
        "EN" -> LANG.ENGLISH
        "ES" -> LANG.SPANISH
        "FR" -> LANG.FRENCH
        "DE" -> LANG.GERMAN
        "UK" -> LANG.UKRAINIAN
        "ZH" -> LANG.CHINESE
        "KO" -> LANG.KOREAN
        "JA" -> LANG.JAPANESE
        "IT" -> LANG.ITALIAN
        "PT" -> LANG.PORTUGUESE
        "AR" -> LANG.ARABIC
        "TR" -> LANG.TURKISH
        "NL" -> LANG.DUTCH
        "PL" -> LANG.POLISH
        "SV" -> LANG.SWEDISH
        "DA" -> LANG.DANISH
        "FI" -> LANG.FINNISH
        "NO" -> LANG.NORWEGIAN
        "EL" -> LANG.GREEK
        "CS" -> LANG.CZECH
        "HE" -> LANG.HEBREW
        "TH" -> LANG.THAI
        "ID" -> LANG.INDONESIAN
        "VI" -> LANG.VIETNAMESE
        "HI" -> LANG.HINDI
        "MS" -> LANG.MALAY
        "TA" -> LANG.TAMIL
        "TE" -> LANG.TELUGU
        "BN" -> LANG.BENGALI
        "HU" -> LANG.HUNGARIAN
        "RO" -> LANG.ROMANIAN
        else -> LANG.UNKNOWN
    }
}