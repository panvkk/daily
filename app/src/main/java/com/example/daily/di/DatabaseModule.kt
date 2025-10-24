package com.example.daily.di

import android.content.Context
import androidx.room.Room
import com.example.daily.data.local.DailyDatabase
import com.example.daily.data.local.dao.MarkDao
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
    fun provideDailyDatabase(@ApplicationContext appContext: Context) : DailyDatabase =
        Room.databaseBuilder(appContext, DailyDatabase::class.java, "daily_db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideMarkDao(db: DailyDatabase) : MarkDao = db.markDao()
}