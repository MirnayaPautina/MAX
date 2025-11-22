package ru.oneme.app.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.oneme.app.R
import ru.oneme.app.data.Country
import ru.oneme.app.data.countries
import ru.oneme.app.presentation.utils.SpacedPhoneVisualTransformation
import ru.oneme.app.presentation.viewmodel.PhoneAuthViewModel
import ru.oneme.app.ui.theme.AshyGray
import ru.oneme.app.ui.theme.ChineseBlack
import ru.oneme.app.ui.theme.Gray
import ru.oneme.app.ui.theme.MAXTheme
import ru.oneme.app.ui.theme.SilverSand

@Composable
fun PhoneInputScreen(
    phone: String,
    onPhoneChange: (String) -> Unit,
    onVerify: () -> Unit,
    viewModel: PhoneAuthViewModel = viewModel()
) {
    val isLightTheme = !isSystemInDarkTheme()
    val selectedCountry = viewModel.selectedCountry

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLightTheme) {
            Image(
                painter = painterResource(id = R.drawable.light_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.dark_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(34.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = if (isLightTheme) R.drawable.max_logo_light
                        else R.drawable.max_logo_dark
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.28f)
                        .sizeIn(
                            maxHeight = 80.dp
                        )
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "С каким номером\nтелефона хотите войти?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "На него придёт СМС с кодом",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(40.dp))

            PhoneNumberFieldWithCountry(
                phone = phone,
                onPhoneChange = onPhoneChange,
                selectedCountry = selectedCountry,
                onCountryClick = { expanded = true },
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Для входа нужен номер из России или страны из \n" +
                        "списка — нажмите на флаг, чтобы выбрать",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onVerify,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(15.dp),
                enabled = phone.length == 10 // Enable only when phone number in entered
            ) {
                Text(
                    text = "Продолжить",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = "Нажимая «Продолжить», вы принимаете политику \n" +
                        "конфиденциальности и пользовательское соглашение",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }

    CountryBottomSheet(
        expanded = expanded,
        countries = countries,
        onCountryChange = { country ->
            viewModel.updateSelectedCountry(country)
        },
        onDismiss = { expanded = false }
    )
}

@Composable
fun PhoneNumberFieldWithCountry(
    phone: String,
    onPhoneChange: (String) -> Unit,
    selectedCountry: Country,
    onCountryClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        // Country Selector
        Box(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            // Country dropdown trigger
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCountryClick() }
                    .padding(vertical = 16.dp, horizontal = 12.dp),
            ) {
                Text(
                    text = "${selectedCountry.flag} ${selectedCountry.dialCode}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                    contentDescription = "Select country",
                    tint = Gray
                )
            }
        }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        // Phone number input
        BasicTextField(
            value = phone,
            onValueChange = { new ->
                onPhoneChange(new.filter { it.isDigit() }.take(10))
            },
            modifier = Modifier
                .weight(1f)
                .focusable()
                .focusRequester(focusRequester)
                .padding(vertical = 16.dp, horizontal = 0.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            visualTransformation = SpacedPhoneVisualTransformation(), // delete for preview
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (phone.isEmpty()) {
                        Text(
                            "123 456 78 90",
                            color = Gray,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Box(Modifier.fillMaxWidth()) {
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun CountryBottomSheet(
    expanded: Boolean,
    countries: List<Country>,
    onCountryChange: (Country) -> Unit,
    onDismiss: () -> Unit
) {
    if (!expanded) return

    val isLightTheme = !isSystemInDarkTheme()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = ChineseBlack.copy(alpha = 0.5f)
            )
            .padding(top = 12.dp, bottom = 0.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(42.dp)
                    .height(4.dp)
                    .background(
                        color = if (isLightTheme) SilverSand else AshyGray,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // scrollable country list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                items(countries) { country ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCountryChange(country)
                                onDismiss()
                            }
                            .padding(vertical = 17.9.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = country.flag,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 13.sp
                            )
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        Text(
                            text = country.name,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = country.dialCode,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp
                            ),
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneInputScreenPreviewLight() {
    MAXTheme(darkTheme = false) {
        PhoneInputScreen(
            phone = "123 456 78 90",
            onPhoneChange = { /* preview doesn't need real implementation */ },
            onVerify = {}
        )
    }
}
