package ru.oneme.app.presentation.navigation

sealed class Screen(val route: String) {
    object PhoneInputScreen : Screen("phone_input")
    object VerificationCodeScreen : Screen(
        "verification_code/{dialCode}/{phone}"
    ) {
        fun createRoute(phone: String, dialCode: String)
        = "verification_code/$dialCode/$phone"
    }
}