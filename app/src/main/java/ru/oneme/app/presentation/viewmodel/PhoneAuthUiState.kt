package ru.oneme.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.oneme.app.data.Country
import ru.oneme.app.data.countries

class PhoneAuthViewModel : ViewModel() {
    var phone by mutableStateOf("")
        private set

    private val _dialCode = MutableStateFlow("+7")
    val dialCode: String get() = _dialCode.value

    var selectedCountry by mutableStateOf(countries[0])
        private set

    fun updatePhone(value: String) {
        phone = value
    }

    fun updateDialCode(code: String) {
        _dialCode.value = code
    }

    fun updateSelectedCountry(country: Country) {
        selectedCountry = country
        updateDialCode(country.dialCode)
    }
}