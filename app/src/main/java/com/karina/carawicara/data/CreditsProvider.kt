package com.karina.carawicara.data

object CreditsProvider {
    fun getAppCredits(): List<AppCredit> {
        return listOf(
            AppCredit(
                category = "Gambar & Ilustrasi",
                items = listOf(
                    CreditItem(
                        title = "Flashcard Images",
                        source = "Freepik",
                        type = "image",
                        license = "Free License",
                        url = "https://www.freepik.com"
                    ),
                    CreditItem(
                        title = "UI Icons",
                        source = "Material Design Icons",
                        type = "image",
                        license = "Apache License 2.0",
                        url = "https://fonts.google.com/icons"
                    ),
                )
            ),
            AppCredit(
                category = "Library & Framework",
                items = listOf(
                    CreditItem(
                        title = "Jetpack Compose",
                        source = "Google/Android",
                        type = "library",
                        license = "Apache License 2.0",
                        url = "https://developer.android.com/jetpack/compose"
                    ),
                    CreditItem(
                        title = "Room Database",
                        source = "Google/Android",
                        type = "library",
                        license = "Apache License 2.0"
                    ),
                    CreditItem(
                        title = "Kotlin Coroutines",
                        source = "JetBrains",
                        type = "library",
                        license = "Apache License 2.0"
                    )
                )
            )
        )
    }

    fun getAppInfo(): Map<String, String> {
        return mapOf(
            "App Name" to "CaraWicara",
            "Version" to "1.0.0",
            "Developer" to "Karina Development Team",
        )
    }
}