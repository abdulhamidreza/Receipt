package com.valiance.receipt.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ReceiptRepository(private val receiptDao: ReceiptDao) {

    val allReceipts: Flow<MutableList<Receipt>> = receiptDao.getAllReceipts()

    fun getReceipt(receiptId: String): Receipt {
        return receiptDao.getReceipt(receiptId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllReceiptOnce(): List<Receipt> {
        return receiptDao.getAllReceiptOnce()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertReceipt(receipt: Receipt) {
        receiptDao.insertReceipt(receipt)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateReceipt(receipt: Receipt) {
        receiptDao.updateReceipt(receipt)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteReceipt(receipt: Receipt) {
        receiptDao.deleteReceipt(receipt)
    }

}