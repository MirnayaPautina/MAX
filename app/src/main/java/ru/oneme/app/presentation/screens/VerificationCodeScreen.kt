package ru.oneme.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.oneme.app.R
import ru.oneme.app.presentation.utils.formatPhoneNumber
import ru.oneme.app.ui.theme.MAXTheme
import ru.oneme.app.ui.theme.StormGray

@Composable
fun VerificationCodeScreen(
    phoneNumber: String,
    dialCode: String,
    code: String,
    onCodeChange: (String) -> Unit,
    onBack: () -> Unit
) {
    var showError by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableIntStateOf(60) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // countdown timer
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime -= 1
            }
        }
        isTimerRunning = false
    }

    // show error when 6 digits entered
    LaunchedEffect(code) {
        if (code.length == 6) {
            showError = true
            delay(1500)
            showError = false
            onCodeChange("")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // moves content above the keyboard
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Back button and header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_return_button),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(42.dp))

            Text(
                text = "Какой код пришёл в СМС?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // subtitle with phone number
            Text(
                text = "Отправили код на $dialCode" +
                        " ${formatPhoneNumber(phoneNumber)}." +
                        "\nЕсли номер неверный, вернитесь назад",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Code input field
            VerificationCodeField(
                code = code,
                onCodeChange = onCodeChange,
                error = if (showError) "Неверный код" else null,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            if (isTimerRunning) {

                val minutes = remainingTime / 60
                val seconds = remainingTime % 60

                // display timer
                Text(
                    text = "Получить новый код можно через $minutes:${seconds.toString()
                        .padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = StormGray
                )
            } else {
                Text(
                    text = "Получить новый код",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .clickable {
                            remainingTime = 60
                            isTimerRunning = true
                        }
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun VerificationCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = modifier) {
        // Limit to 6 digits and only allow numbers
        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                // Only allow digits and limit to 6 characters
                val filtered = newValue.filter { it.isDigit() }.take(6)
                onCodeChange(filtered)

                if (filtered.length == 6) {
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusable()
                .focusRequester(focusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
//            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Code digits display
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 6) {
                            val digit = if (i < code.length) code[i].toString() else ""
                            val isFocused = i == code.length
                            val showCursor = i == code.length && error == null

                            Box(
                                modifier = Modifier
                                    .size(
                                        width = 42.dp,
                                        height = 55.dp
                                    )
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (digit.isNotEmpty()) {
                                    Text(
                                        text = digit,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = if (error != null) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                } else if (showCursor) {
                                    BlinkingCursor()
                                }
                            }
                        }
                    }

                    // Hidden text field for input
                    Box(
                        modifier = Modifier
                            .height(0.dp)
                            .alpha(0f)
                    ) {
                        innerTextField()
                    }
                }
            }
        )

        // Error message
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun BlinkingCursor(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            visible = !visible
        }
    }

    if (visible) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(28.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodeScreenPreview() {
    MAXTheme {
        VerificationCodeScreen(
            phoneNumber = " 944 856 25 63",
            dialCode = "+7",
            code = "123",
            onCodeChange = { },
            onBack = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodeScreenPreviewDark() {
    MAXTheme(darkTheme = true) {
        VerificationCodeScreen(
            phoneNumber = " 944 856 25 63",
            dialCode = "+7",
            code = "123",
            onCodeChange = { },
            onBack = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodeScreenErrorPreview() {
    MAXTheme {
        VerificationCodeScreen(
            phoneNumber = " 944 856 25 63",
            dialCode = "+7",
            code = "123",
            onCodeChange = { },
            onBack = { }
        )
    }
}