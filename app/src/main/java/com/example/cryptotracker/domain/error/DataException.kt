package com.example.cryptotracker.domain.error

// Что пошло не так при загрузке, на языке домена. Репозиторий превращает IOException и HttpException
// в эти типы, поэтому ViewModel и UI не знают ни про Retrofit, ни про OkHttp.
sealed class DataException(cause: Throwable?) : Exception(cause) {
    class Network(cause: Throwable? = null) : DataException(cause)
    class Server(cause: Throwable? = null) : DataException(cause)
}
