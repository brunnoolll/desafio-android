package com.picpay.desafio.android.domain.exception

import java.io.IOException

sealed class ApiException(message: String) : IOException(message) {
    class NetworkError(message: String = "Verifique sua conexão de rede.") : ApiException(message)
    class UnauthorizedError(message: String = "Sessão expirada. Por favor, faça login novamente.") : ApiException(message)
    class ServerError(message: String = "Ocorreu um erro no servidor. Tente novamente mais tarde.") : ApiException(message)
    class UnknownError(message: String = "Ocorreu um erro inesperado.") : ApiException(message)
}