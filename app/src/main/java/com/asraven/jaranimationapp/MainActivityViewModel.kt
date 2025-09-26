package com.asraven.jaranimationapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asraven.jaranimationapp.data.remote.EducationCard
import com.asraven.jaranimationapp.data.remote.SaveButtonCta
import com.asraven.jaranimationapp.domain.usecase.GetEducationMetadataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getEducationMetadataUseCase: GetEducationMetadataUseCase // Inject the use case
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainActivityUiState> = MutableStateFlow(
        MainActivityUiState()
    )
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
         fetchEducationData()
    }

    fun fetchEducationData() {
        _uiState.update { it.copy(isLoadingEducationData = true, educationDataError = null) }

        viewModelScope.launch { // Use viewModelScope for coroutines tied to ViewModel lifecycle
            getEducationMetadataUseCase()
                .onEach { result ->
                    result.fold(
                        onSuccess = { onboardingApiResponse ->

                            _uiState.update {
                                it.copy(
                                    educationItems = onboardingApiResponse.data?.manualBuyEducationData?.educationCardList ?: emptyList(), // Directly assign if it's List<EducationItem>
                                    educationDataError = null,
                                    intro = Intro(
                                        title = onboardingApiResponse.data?.manualBuyEducationData?.introTitle ,
                                        subTitle = onboardingApiResponse.data?.manualBuyEducationData?.introSubtitle
                                    ),
                                    saveButtonCta =  onboardingApiResponse.data?.manualBuyEducationData?.saveButtonCta,
                                    isLoadingEducationData = false
                                )
                            }
                        },
                        onFailure = { throwable ->
                            _uiState.update {
                                it.copy(
                                    isLoadingEducationData = false,
                                    educationDataError = throwable
                                )
                            }
                        }
                    )
                }
                .catch { exception -> // Catch exceptions from the flow itself (e.g., if use case throws directly)
                    _uiState.update {
                        it.copy(
                            isLoadingEducationData = false,
                            educationDataError = exception
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}

data class MainActivityUiState(
    val educationItems: List<EducationCard> = emptyList(),
    val isLoadingEducationData: Boolean = false,
    val educationDataError: Throwable? = null,
    val intro: Intro? = null,
    val saveButtonCta: SaveButtonCta? = null,
)

data class Intro(
    val title: String?,
    val subTitle : String?,
)