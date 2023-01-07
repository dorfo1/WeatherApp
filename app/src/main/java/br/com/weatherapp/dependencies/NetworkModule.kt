package br.com.weatherapp.dependencies

import br.com.weatherapp.base.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    val moduleDependencies = module {
        single { provideRetrofit(get()) }
        single { provideOkHttpClient(get()) }
        single { AuthInterceptor() }
    }

    private fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.addInterceptor(authInterceptor)
        okHttpClient.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        return okHttpClient.build()
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }
}