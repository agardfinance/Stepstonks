package com.stepstonks.app.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.stepstonks.app.MainActivity
import com.stepstonks.app.R
import com.stepstonks.app.data.local.preferences.UserPreferencesDataStore
import com.stepstonks.app.domain.model.TransactionType
import com.stepstonks.app.domain.model.earningMultiplier
import com.stepstonks.app.domain.repository.StepRepository
import com.stepstonks.app.domain.repository.TokenRepository
import com.stepstonks.app.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import com.stepstonks.app.domain.model.TokenTransaction
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {

    @Inject lateinit var stepRepository: StepRepository
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var tokenRepository: TokenRepository
    @Inject lateinit var prefs: UserPreferencesDataStore

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    private var serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var sessionSteps = 0L
    private var sessionStartSteps = -1L
    private var tokensEarnedThisSession = 0.0
    private var xpEarnedThisSession = 0L
    private var lastSaveTime = 0L

    companion object {
        const val CHANNEL_ID = "step_counter_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START = "START_TRACKING"
        const val ACTION_STOP = "STOP_TRACKING"

        fun startService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, StepCounterService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking() {
        startForeground(NOTIFICATION_ID, buildNotification(0L, 0.0))
        val sensor = stepSensor ?: stepDetectorSensor
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        schedulePeriodicSave()
    }

    private fun stopTracking() {
        sensorManager.unregisterListener(this)
        serviceScope.launch { saveProgress() }
        serviceScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun schedulePeriodicSave() {
        serviceScope.launch {
            while (isActive) {
                delay(30_000)
                saveProgress()
            }
        }
    }

    private suspend fun saveProgress() {
        if (sessionSteps == 0L) return
        val user = userRepository.getCurrentUser().firstOrNull() ?: return
        if (user.energy <= 0f) return

        val multiplier = user.earningMultiplier()
        val equippedSneaker = null
        val baseTokens = sessionSteps * 0.01
        val totalTokens = baseTokens * multiplier
        val xp = (sessionSteps / 10).toLong()

        stepRepository.updateTodaySteps(sessionSteps, totalTokens, xp)
        userRepository.addTokens(user.id, totalTokens)
        userRepository.addXp(user.id, xp)

        tokenRepository.recordTransaction(
            TokenTransaction(
                type = TransactionType.EARNED_WALKING,
                amount = totalTokens,
                description = "Earned from ${sessionSteps} steps",
                balanceAfter = user.tokenBalance + totalTokens
            )
        )

        tokensEarnedThisSession += totalTokens
        xpEarnedThisSession += xp

        val energyUsed = (sessionSteps / 200f).coerceAtMost(user.energy)
        userRepository.updateEnergy(user.id, (user.energy - energyUsed).coerceAtLeast(0f))

        updateNotification(sessionSteps, tokensEarnedThisSession)
        lastSaveTime = System.currentTimeMillis()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val totalSteps = event.values[0].toLong()
                if (sessionStartSteps == -1L) {
                    sessionStartSteps = totalSteps
                }
                sessionSteps = totalSteps - sessionStartSteps
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                sessionSteps++
            }
        }
        updateNotification(sessionSteps, tokensEarnedThisSession)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_step_counter),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.channel_step_counter_desc)
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(steps: Long, tokens: Double): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🚶 ${steps} steps • +${String.format("%.2f", tokens)} STK")
            .setContentText("Keep walking to earn more STEPSTONKS!")
            .setSmallIcon(android.R.drawable.ic_menu_directions)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification(steps: Long, tokens: Double) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, buildNotification(steps, tokens))
    }
}
