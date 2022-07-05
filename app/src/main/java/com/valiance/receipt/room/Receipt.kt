package com.valiance.receipt.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Receipts")
data class Receipt(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "receiptId") var receiptId: Long,
    @ColumnInfo(name = "createdDate") var createdDate: String,
    @ColumnInfo(name = "priceP1") var priceP1: Double,
    @ColumnInfo(name = "quantityP1") var quantityP1: Int,
    @ColumnInfo(name = "priceP2") var priceP2: Double,
    @ColumnInfo(name = "quantityP2") var quantityP2: Int,
    @ColumnInfo(name = "priceP3") var priceP3: Double,
    @ColumnInfo(name = "quantityP3") var quantityP3: Int,
    @ColumnInfo(name = "filePath") var filePath: String,
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
