package ir.fallahpoor.vicinity.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.data.entity.Venues
import ir.fallahpoor.vicinity.data.repository.dao.VenuesDao

@androidx.room.Database(
    entities = [VenueEntity::class, Venues::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun venuesDao(): VenuesDao

    companion object {

        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {

            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        Database::class.java,
                        "database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE!!

        }

        fun destroy() {
            INSTANCE = null
        }

    }

}