object HttpConstants {
    const val USER_STATUS_REQUEST = "user/status"
    const val USER_SIGN_IN_REQUEST = "user/signin"
    const val USER_SIGN_UP_REQUEST = "user/signup"
    const val USER_INFO_REQUEST = "user/info"

    const val AUTHORS_SEARCH_REQUEST = "authors/search"

    const val AUTHOR_BOOKS_REQUEST = "books/byauthor"
    const val BOOKS_SEARCH_REQUEST = "books/search"
    const val GET_ALL_NON_MODERATING_BOOKS = "books/moderating"
    const val SET_APPROVED_NON_MODERATING_BOOKS = "books/moderating/approve"
    const val SET_DISCARDED_NON_MODERATING_BOOKS = "books/moderating/discard"
    const val UPLOAD_BOOK_IMAGE = "books/image/upload"
    const val SET_BOOK_AS_APPROVED_WITHOUT_UPLOAD_IMAGE = "books/moderating/approvewithouimage"
    const val ADD_NEW_BOOK_BY_USER = "books/users/add"
    const val UPDATE_USER_BOOK = "books/users/update"

    const val SYNCHRONIZE_USER_DATA = "sync/user/data"
}