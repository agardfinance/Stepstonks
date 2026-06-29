package com.stepstonks.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.stepstonks.app.data.local.database.dao.*
import com.stepstonks.app.data.local.database.entity.*

@Database(
    entities = [
        UserEntity::class,
        StepEntity::class,
        ChallengeEntity::class,
        TokenTransactionEntity::class,
        SneakerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StepstonksDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun stepDao(): StepDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun tokenDao(): TokenDao
    abstract fun sneakerDao(): SneakerDao

    companion object {
        @Volatile private var INSTANCE: StepstonksDatabase? = null

        fun getInstance(context: Context): StepstonksDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StepstonksDatabase::class.java,
                    "stepstonks.db"
                ).build().also { INSTANCE = it }
            }
    }
}
