package ru.oneme.app.data

data class Country(
    val name: String,
    val dialCode: String,
    val flag: String
)

val countries = listOf(
    Country("Россия", "+7", "🇷🇺"),
    Country("Беларусь", "+375", "🇧🇾"),
    Country("Азербайджан", "+994", "🇦🇿"),
    Country("Армения", "+374", "🇦🇲"),
    Country("Казахстан", "+7", "🇰🇿"),
    Country("Киргизия", "+996", "🇰🇬"),
    Country("Молдова", "+373", "🇲🇩"),
    Country("Таджикистан", "+992", "🇹🇯"),
    Country("Узбекистан", "+998", "🇺🇿")
)
