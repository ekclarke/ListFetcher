package com.example.listfetcher.data

import com.example.listfetcher.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class InitialSyncUseCase @Inject constructor(
    private val repository: RepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
){
    suspend operator fun invoke() {

    }

}
