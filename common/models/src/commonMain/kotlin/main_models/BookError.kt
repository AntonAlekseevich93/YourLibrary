package main_models

class BookError(
    type: ErrorType
)

enum class ErrorType{
    PARSE_ERROR_NOT_CORRECT_URL
}