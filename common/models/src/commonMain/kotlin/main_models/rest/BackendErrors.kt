package main_models.rest

sealed class BackendErrors {
    data object UNKNOWN : BackendErrors()
}