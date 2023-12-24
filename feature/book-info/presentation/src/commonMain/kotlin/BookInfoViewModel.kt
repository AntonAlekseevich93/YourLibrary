import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import main_models.BookItemVo
import models.BookInfoUiState

class BookInfoViewModel(private val repository: BookInfoRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    fun getBookItem(id: Int){
        //todo получаем книгу из бд
        _uiState.value.bookItem.value = BookItemVo(
            id = 1,
            shelfId = 1,
            numbersOfPage = 265,
            isbn = "978-5-6044959-5-7",
            bookName = "Секс. Все, что вы хотели узнать о сексе, но боялись спросить: от анатомии до психологии",
            authorName = "Стивен кинг",
            coverUrl = "https://woody-comics.ru/images/detailed/31/c73b1e8b1f63850e835232cb1a07be0d.jpg",
            description = "Роман «Джейн Эйр» вышел в 1847 году и стал главной книгой британской писательницы Шарлотты Бронте, ее визитной карточкой. Кто-то остроумно заметил: даже те, кто не удосужился прочесть роман, знают о существовании его героини — невзрачной гувернантки, нашедшей после многих горестей свое счастье. Со времени выхода романа специалисты пытаются объяснить и примирить его очевидное несовершенство и захватывающий интерес к нему. Отмечают, что Шарлотта Бронте первой сделала героиней дурнушку. Подчеркивают, что Джейн Эйр — первая героиня, которая сама рассказывает свою историю. Упирают на универсальный расклад дамского романа: декорации — хоть замок с привидениями, хоть Рублевка, хоть Голливуд. Вирджиния Вульф в свое время заметила: «Шарлотта все свое красноречие, страсть и богатство стиля употребляла для того, чтобы выразить простые вещи: “Я люблю”, “Я ненавижу”, “Я страдаю”». А что еще нужно читательнице? Ведь каждая Джейн Эйр ждет своего мистера Рочестера и надеется однажды сказать: «Читатель, я вышла за него замуж». При выпуске классических книг нам, издательству «Время», очень хотелось создать действительно современную серию, показать живую связь неувядающей классики и окружающей действительности. Поэтому мы обратились к известным литераторам, ученым, журналистам и деятелям культуры с просьбой написать к выбранным ими книгам сопроводительные статьи — не сухие пояснительные тексты и не шпаргалки к экзаменам, а своего рода объяснения в любви дорогим их сердцам авторам. У кого-то получилось возвышенно и трогательно, у кого-то посуше и поакадемичней, но это всегда искренне и интересно, а иногда — неожиданно и необычно. В любви к «Джейн Эйр» признаётся переводчик и литературный критик Наталья Игрунова — книгу стоит прочесть уже затем, чтобы сверить своё мнение со статьёй и взглянуть на произведение под другим углом."
        )
    }

}