package com.melonlemon.rentcalendar.core.domain.model

import androidx.room.*
import java.time.LocalDate


@Entity(tableName = "flats", indices = [Index(value = arrayOf("name"), unique = true)])
data class Flats(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val active: Boolean
)

@Entity(
    tableName = "schedule",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Payment::class,
            parentColumns = ["id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "flat_id")  val flatId: Int,
    val year: Int,
    val month: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    @ColumnInfo(name = "person_id")  val personId: Int,
    @ColumnInfo(name = "payment_id")  val paymentId: Int,
    val comment: Int
)

@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val phone: Int?
)

@Entity(tableName = "payment",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "flat_id")  val flatId: Int,
    val year: Int,
    val month: Int,
    val nights: Int,
    val paymentSingleNight: Int,
    val paymentAllNights: Int,
    val isPaid: Boolean
)

@Entity(tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Expenses(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "flat_id")  val flatId: Int,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    val amount: Int,
    val comment: Int
)

@Entity(tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = CategoryType::class,
            parentColumns = ["id"],
            childColumns = ["type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "type_id") val typeId: Int,
    val name: String,
    val fixedAmount: Int,
    val active: Boolean
)

@Entity(tableName = "categoryType")
data class CategoryType(
    @PrimaryKey val id: Int,
    val isRegular: Boolean,
    val isFixed: Boolean
)

//Supported classes
data class AmountGroupBy(
    val year: Int,
    val month: Int,
    val amount: Int
)

data class FullRentInfo(
    @Embedded val schedule: Schedule,
    @Relation(
    parentColumn = "person_id",
    entityColumn = "id"
    )
    val person: Person,
    @Relation(
        parentColumn = "payment_id",
        entityColumn = "id"
    )
    val payment: Payment
)