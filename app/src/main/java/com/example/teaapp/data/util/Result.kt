package com.example.teaapp.data.util

/**
 * Represents the state of a data-fetching operation.
 */
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String?) : Result<Nothing>()
}