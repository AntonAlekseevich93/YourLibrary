package main_models.service_development

import main_models.BookVo

data class BookWithServiceDevelopment(
    val book: BookVo,
    val serviceDevelopmentBookVo: UserServiceDevelopmentBookVo
)