package me.karounwi.common.githubprofile.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.karounwi.common.githubprofile.GithubProfileModule
import me.karounwi.common.githubprofile.domain.usecase.GetProfilesUseCase
import me.karounwi.common.util.DataState
import me.karounwi.common.util.Event

class GithubProfileViewModelImpl constructor(private val getProfilesUseCase: GetProfilesUseCase): GithubProfileViewModel {
    private val _dataStates: MutableStateFlow<DataState<GithubProfileViewData>> = MutableStateFlow(DataState.Success(null))
    override val dataStates: StateFlow<DataState<GithubProfileViewData>> = _dataStates

    private lateinit var scope: CoroutineScope

    override fun provideScope(coroutineScope: CoroutineScope) {
        scope = coroutineScope
    }

    override fun getProfiles() {
        scope.launch {
            val currentViewData = _dataStates.value.toData()
            try {
                _dataStates.value = DataState.Loading(currentViewData)
                val profiles = getProfilesUseCase()
                _dataStates.value = DataState.Success(GithubProfileViewData(profiles))
            } catch (exception: Exception) {
                _dataStates.value = DataState.Error(currentViewData, Event(exception))
            }
        }
    }

    companion object {
        fun getInstance(): GithubProfileViewModelImpl {
            return GithubProfileViewModelImpl(GithubProfileModule.getProfileUseCase())
        }
    }
}