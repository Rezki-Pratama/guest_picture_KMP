package com.sloth.guess_picture_app.utils.localization
import kotlinx.serialization.json.*

// How to use
// val localizationManager = JsonLocalizationManager(jsonString)
// println(localizationManager.currentResources.getString("welcome"))
// println(localizationManager.currentResources.getString("login"))

class JsonStringResources(private val translations: Map<String, String>) : IStringResources {
    override fun getString(key: String): String {
        return translations[key] ?: key // Return key if translation not found
    }
}

class LocalizationManager(private val jsonString: String) {
    private val localizedStrings: Map<String, IStringResources>

    init {
        localizedStrings = parseJson(jsonString)
    }

    // Automatically get resources for the current locale
    val currentResources: IStringResources
        get() {
            val locale = getCurrentLocale() // Detects the current locale
            return localizedStrings[locale] ?: JsonStringResources(emptyMap()) // Fallback to empty resources
        }

    // Parses the JSON and converts it into a Map of language -> StringResources
    private fun parseJson(json: String): Map<String, IStringResources> {
        val parsed = Json.decodeFromString<Map<String, Map<String, String>>>(json)
        return parsed.mapValues { (_, translations) -> JsonStringResources(translations) }
    }

    // This function returns the current locale (this can be customized based on platform)
    private fun getCurrentLocale(): String {


        // For example, return "en" for English, "pl" for Polish, etc.
        // This can be improved by detecting the locale from system settings (e.g., using Locale.getDefault() on Android)
        // return Locale.current.language
        return "en" // Placeholder for actual locale detection logic
    }
}