package com.example.buchbrowser.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.buchbrowser.ui.theme.AppThemeMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class ThemePreferencesRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `setAppThemeMode persists selected mode`() = runTest {
        val repository = ThemePreferencesRepository(
            dataStore = PreferenceDataStoreFactory.create(
                scope = backgroundScope,
                produceFile = {
                    temporaryFolder.newFile("theme.preferences_pb")
                },
            ),
        )

        repository.setAppThemeMode(AppThemeMode.SUNSET)

        assertEquals(AppThemeMode.SUNSET, repository.appThemeMode.first())
    }
}
