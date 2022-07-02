package com.valiance.receipt.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        Receipt::class
    ],
    version = 1,
    exportSchema = false
) //exportSchema = false true In case of Migration
public abstract class CustRoomDatabase : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao

    companion object {

        @Volatile    //This will guarantee visibility of changes for other threads as soon as the value is changed
        private var INSTANCE: CustRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CustRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustRoomDatabase::class.java,
                    "receiptDb"
                ).build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

}