package br.com.weatherapp.base

sealed class Resource<T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T> : Resource<T>()
    class Error<T>(exception: Exception) : Resource<T>(exception = exception)
}