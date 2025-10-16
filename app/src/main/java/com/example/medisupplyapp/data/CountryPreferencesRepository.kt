package com.example.medisupplyapp.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

private val Context.dataStore by preferencesDataStore(name = "regional_settings")

object CountryPreferencesKeys {
    val SELECTED_COUNTRY = stringPreferencesKey("selected_country")
}

class CountryPreferencesRepository(private val context: Context) {

    val selectedCountry: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CountryPreferencesKeys.SELECTED_COUNTRY] ?: "Colombia"
    }

    suspend fun saveCountry(country: String) {
        context.dataStore.edit { preferences ->
            preferences[CountryPreferencesKeys.SELECTED_COUNTRY] = country
        }
    }
}
