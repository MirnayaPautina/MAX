package ru.oneme.app.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class SpacedPhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val out = formatPhoneNumber(text.text)

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 7) return offset + 2
                if (offset <= 9) return offset + 3
                return out.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 10) return offset - 2
                if (offset <= 13) return offset - 3
                return 10
            }
        }

        return TransformedText(androidx.compose.ui.text.AnnotatedString(out), offsetTranslator)
    }
}

fun formatPhoneNumber(phone: String): String {
    val trimmed = phone.take(10).filter { it.isDigit() }

    return buildString {
        for (i in trimmed.indices) {
            append(trimmed[i])
            when (i) {
                2, 5, 7 -> append(" ")
            }
        }
    }
}