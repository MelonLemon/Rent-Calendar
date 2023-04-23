package com.melonlemon.rentcalendar.core.domain.model

import androidx.room.*
import java.time.LocalDate


@Entity(tableName = "flats")
data class Flats(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "flat_id", index = true) val id: Int?,
    val name: String,
    val active: Boolean
)

@Entity
data class Currency(
    val currency: String
)

@Entity(
    tableName = "schedule",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["flat_id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Person::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Payment::class,
            parentColumns = ["payment_id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "schedule_id", index = true)  val id: Int?,
    @ColumnInfo(name = "flat_id", index = true)  val flatId: Int,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "start_date") val startDate: LocalDate,
    @ColumnInfo(name = "end_date") val endDate: LocalDate,
    @ColumnInfo(name = "person_id", index = true)  val personId: Int,
    @ColumnInfo(name = "payment_id", index = true)  val paymentId: Int,
    val comment: String
)

@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "person_id", index = true) val id: Int?,
    val name: String,
    val phone: Int?
)

@Entity(tableName = "payment",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["flat_id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "payment_id", index = true) val id: Int?,
    @ColumnInfo(name = "flat_id", index = true)  val flatId: Int,
    val year: Int,
    val month: Int,
    val nights: Int,
    val paymentSingleNight: Int,
    val paymentAllNights: Int,
    val paymentDate: LocalDate?=null,
    @ColumnInfo(name = "is_paid") val isPaid: Boolean
)

@Entity(tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Flats::class,
            parentColumns = ["flat_id"],
            childColumns = ["flat_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Expenses(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "expenses_id", index = true) val id: Int?,
    @ColumnInfo(name = "flat_id", index = true)  val flatId: Int,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int,
    val amount: Int,
    val paymentDate: LocalDate,
    val comment: String
)

@Entity(tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = CategoryType::class,
            parentColumns = ["type_id"],
            childColumns = ["type_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )]
)
data class Category(
    @PrimaryKey(autoGenerate = true)  @ColumnInfo(name = "category_id", index = true)  val id: Int?,
    @ColumnInfo(name = "type_id", index = true) val typeId: Int,
    val name: String,
    @ColumnInfo(name = "fix_amount") val fixedAmount: Int,
    val active: Boolean
)

@Entity(tableName = "categoryType")
data class CategoryType(
    @PrimaryKey @ColumnInfo(name = "type_id", index = true) val id: Int,
    val isRegular: Boolean
)

//Supported classes
data class AmountGroupBy(
    val year: Int,
    val month: Int,
    val amount: Int
)

data class TransactionsDay(
    val paymentDate: LocalDate,
    val category: String,
    val amount: Int,
    val comment: String
)

data class FullRentInfo(
    @Embedded val schedule: Schedule,
    @Relation(
    parentColumn = "person_id",
    entityColumn = "person_id"
    )
    val person: Person,
    @Relation(
        parentColumn = "payment_id",
        entityColumn = "payment_id"
    )
    val payment: Payment
)

data class CategoryShortInfo(
    val id: Int,
    val name: String,
    val amount: Int
)

data class MostBookedMonthInfo(
    val month: Int,
    val percent: Int
)

data class BookedDaysPeriods(
    @ColumnInfo(name = "start_date") val startDate: LocalDate,
    @ColumnInfo(name = "end_date") val endDate: LocalDate,
)