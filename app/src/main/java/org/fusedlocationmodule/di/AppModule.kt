package org.fusedlocationmodule.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fusedlocationmodule.api.StudioService
import org.fusedlocationmodule.constant.Constants.Companion.BASE_URL
import org.fusedlocationmodule.permission.PermissionUtils
import org.fusedlocationmodule.repo.LocationRepo
import org.fusedlocationmodule.repo.StudioListRequestRepoImpl
import org.fusedlocationmodule.util.NetworkMonitor
import org.fusedlocationmodule.viewmodel.StudioListViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStudioListViewModel(
        studioListRequestRepo: StudioListRequestRepoImpl
    ): StudioListViewModel {
        return StudioListViewModel(studioListRequestRepo)
    }


    @Provides
    fun provideLocationRepo(
        permissionUtils: PermissionUtils,
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationRepo {
        return LocationRepo(permissionUtils, context, fusedLocationProviderClient)
    }

    @Provides
    @Singleton
    fun provideStudioListRepository(
        locationRepo: LocationRepo,
        studioService: StudioService
    ): StudioListRequestRepoImpl {
        return StudioListRequestRepoImpl(locationRepo, studioService)
    }


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json") // Customize content type here
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideStudioService(retrofit: Retrofit): StudioService {
        return retrofit.create(StudioService::class.java)
    }

    @Provides
    @Singleton
    fun providePermissionUtils(): PermissionUtils {
        return PermissionUtils()
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideNetworkMonitor(connectivityManager: ConnectivityManager): NetworkMonitor {
        return NetworkMonitor(connectivityManager)
    }

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}

