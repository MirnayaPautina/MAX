package ru.oneme.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.oneme.app.presentation.screens.PhoneInputScreen
import ru.oneme.app.presentation.screens.VerificationCodeScreen
import ru.oneme.app.presentation.viewmodel.PhoneAuthViewModel

@Composable
fun MaxNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PhoneInputScreen.route
    ) {
        composable(Screen.PhoneInputScreen.route) {
            val viewModel = viewModel<PhoneAuthViewModel>()

            PhoneInputScreen(
                phone = viewModel.phone,
                onPhoneChange = { viewModel.updatePhone(it) },
                onVerify = {
                    navController.navigate(
                        Screen.VerificationCodeScreen.createRoute(
                            viewModel.phone,
                            viewModel.dialCode
                        )
                    )
                }
            )
        }

        composable(Screen.VerificationCodeScreen.route) { backStackEntry ->
            val dialCode = backStackEntry.arguments?.getString("dialCode") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""

            var code by remember { mutableStateOf("") }

            VerificationCodeScreen(
                phoneNumber = phone,
                code = code,
                dialCode = dialCode,
                onCodeChange = { code = it },
                onBack = { navController.popBackStack() }
            )
        }
    }
}