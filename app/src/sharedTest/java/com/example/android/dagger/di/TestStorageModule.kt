package com.example.android.dagger.di

import com.example.android.dagger.storage.Storage
import dagger.Binds
import dagger.Module
import com.example.android.dagger.storage.FakeStorage

@Module
abstract class TestStorageModule {

    // Makes Dagger provide FakeStorage when a Storage type is requested
    @Binds
    abstract fun provideStorage(storage: FakeStorage): Storage
}