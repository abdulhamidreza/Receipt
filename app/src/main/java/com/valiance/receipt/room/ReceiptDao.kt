package com.valiance.receipt.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM Receipts")
    fun getAllReceipts(): Flow<MutableList<Receipt>> //Flow to observer db changes in real time

    @Query("SELECT * FROM Receipts")
    fun getAllReceiptOnce(): MutableList<Receipt>

    @Query("SELECT * FROM Receipts WHERE receiptId = :pkgName LIMIT 1")
    fun getReceipt(pkgName: String): Receipt

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReceipt(receipt: Receipt)

    @Update
    suspend fun updateReceipt(receipt: Receipt)

    @Delete
    suspend fun deleteReceipt(receipt: Receipt)

}