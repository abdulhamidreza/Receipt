package com.valiance.receipt.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Receipts")
data class Receipt(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "receiptId") val receiptId: Long,
    @ColumnInfo(name = "createdDate") val createdDate: String,
    @ColumnInfo(name = "priceP1") val priceP1: Double,
    @ColumnInfo(name = "quantityP1") val quantityP1: Int,
    @ColumnInfo(name = "priceP2") val priceP2: Double,
    @ColumnInfo(name = "quantityP2") val quantityP2: Int,
    @ColumnInfo(name = "priceP3") val priceP3: Double,
    @ColumnInfo(name = "quantityP3") val quantityP3: Int,
    @ColumnInfo(name = "filePath") val filePath: String,
) {


    constructor(
        createdDate: String,
        priceP1: Double,
        quantityP1: Int,
        priceP2: Double,
        quantityP2: Int,
        priceP3: Double,
        quantityP3: Int,
        filePath: String
    ) : this(
        0,
        createdDate,
        priceP1,
        quantityP1,
        priceP2,
        quantityP2,
        priceP3,
        quantityP3,
        filePath
    )

}
