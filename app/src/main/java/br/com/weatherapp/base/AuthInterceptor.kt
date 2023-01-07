package br.com.weatherapp.base

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private companion object {
        const val APP_KEY_QUERY = "&appid="
        const val APP_KEY_VALUE = "3db67f55fca3648c14979f7f6be50dc8"
        const val LANG_QUERY = "&lang="
        const val PT_BR_LANG = "pt_br"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .url("${request.url}$APP_KEY_QUERY${APP_KEY_VALUE}$LANG_QUERY$PT_BR_LANG")
            .build()

        return chain.proceed(request)
    }
}