package com.sloth.guess_picture_app.di

import com.sloth.guess_picture_app.utils.localization.LocalizationManager
import org.koin.dsl.module

// Define a Koin module
val localizationModule = module {
    // Provide the JSON string resource as a singleton (can be loaded from a file or network)
    single<String> {
        // Simulate loading JSON string from a file or network
        """
        {
          "en": {
            "welcome": "Welcome to Example App",
            "login": "Login"
          },
          "pl": {
            "welcome": "Witaj w przykładowej aplikacji",
            "login": "Zaloguj się"
          }
        }
        """
    }

    // Provide the LocalizationManager as a singleton
    single { LocalizationManager(get()) }
}