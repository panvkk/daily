package com.example.daily.di

import android.content.Context
import androidx.room.Room
import com.example.daily.data.local.Converters
import com.example.daily.data.local.DailyDatabase
import com.example.daily.data.local.dao.MarkDao
import com.example.daily.data.local.dao.TopicDao
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMoshi() : Moshi =
        Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    @Provides
    @Singleton
    fun provideDailyDatabase(
        @ApplicationContext appContext: Context,
        moshi: Moshi
    ) : DailyDatabase =
        Room.databaseBuilder(appContext, DailyDatabase::class.java, "daily_db")
            .fallbackToDestructiveMigration(false)
            .addTypeConverter(Converters(moshi))
            .build()

    @Provides
    fun provideMarkDao(db: DailyDatabase) : MarkDao = db.markDao()

    @Provides
    fun provieTopicDao(db: DailyDatabase) : TopicDao = db.topicDao()
}