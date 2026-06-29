package com.stepstonks.app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stepstonks_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
        val DAILY_GOAL_STEPS = longPreferencesKey("daily_goal_steps")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val STEP_TRACKING_ENABLED = booleanPreferencesKey("step_tracking_enabled")
        val LAST_DAILY_RESET = longPreferencesKey("last_daily_reset")
        val LAST_WEEKLY_RESET = longPreferencesKey("last_weekly_reset")
        val THEME_DARK = booleanPreferencesKey("theme_dark")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }

    val userId: Flow<String?> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.USER_ID] }

    val isOnboarded: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.IS_ONBOARDED] ?: false }

    val dailyGoalSteps: Flow<Long> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.DAILY_GOAL_STEPS] ?: 10_000L }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.NOTIFICATIONS_ENABLED] ?: true }

    val stepTrackingEnabled: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.STEP_TRACKING_ENABLED] ?: true }

    val lastDailyReset: Flow<Long> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.LAST_DAILY_RESET] ?: 0L }

    suspend fun setUserId(id: String) = context.dataStore.edit { it[Keys.USER_ID] = id }
    suspend fun setOnboarded(value: Boolean) = context.dataStore.edit { it[Keys.IS_ONBOARDED] = value }
    suspend fun setDailyGoal(steps: Long) = context.dataStore.edit { it[Keys.DAILY_GOAL_STEPS] = steps }
    suspend fun setNotifications(enabled: Boolean) = context.dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    suspend fun setStepTracking(enabled: Boolean) = context.dataStore.edit { it[Keys.STEP_TRACKING_ENABLED] = enabled }
    suspend fun setLastDailyReset(timestamp: Long) = context.dataStore.edit { it[Keys.LAST_DAILY_RESET] = timestamp }
    suspend fun setLastWeeklyReset(timestamp: Long) = context.dataStore.edit { it[Keys.LAST_WEEKLY_RESET] = timestamp }
}
