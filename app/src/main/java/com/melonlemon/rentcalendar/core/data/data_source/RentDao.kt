package com.melonlemon.rentcalendar.core.data.data_source

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.melonlemon.rentcalendar.core.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RentDao {

    //ADD/UPDATE FLAT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFlat(flat: Flats)

    //GET ALL FLATS
    @Query("SELECT * FROM flats")
    suspend fun getFlats():List<Flats>

    //ADD/UPDATE PERSON
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPerson(person: Person): Long

    //ADD/UPDATE PAYMENT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPayment(payment: Payment): Long

    //UPDATE PAYMENT STATUS
    @Query("UPDATE payment SET is_paid = isPaid WHERE id=:id")
    suspend fun updatePaymentStatus(id: Int, isPaid: Boolean)

    //UPDATE PAYMENT STATUS
    @Query("UPDATE expenses SET amount = amount WHERE id=:id")
    suspend fun updateExpenses(id: Int, amount: Int)

    //ADD/UPDATE SCHEDULE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(schedule: Schedule)

    //ADD SCHEDULES
    @Transaction
    suspend fun addSchedules(person: Person, payments: List<Payment>, schedules: List<Schedule>){
        val personId = addPerson(person).toInt()
        schedules.forEachIndexed { index, schedule ->
            val paymentId = addPayment(payments[index]).toInt()
            addSchedule(schedule.copy(
                personId = personId,
                paymentId = paymentId
            ))
        }
    }

    //GET FULL RENT INFO BY YEAR&MONTH AND FLAT ID
    @Transaction
    @Query("SELECT * FROM schedule WHERE flat_id=:flatId AND year=:year AND month=:month")
    fun getFullRentInfoByYM(year: Int, month: Int, flatId: Int): Flow<List<FullRentInfo>>

    //GET INCOME GROUP BY MONTH, FILTER YEAR, FLAT AND IS PAID
    @Query("SELECT year, month, SUM(paymentAllNights) AS amount FROM payment WHERE flat_id=:flatId AND year=:year AND isPaid=:isPaid GROUP BY year AND month")
    suspend fun getPaymentGroupByMY(flatId: Int, year: Int, isPaid: Boolean):List<AmountGroupBy>

    //GET ALL INCOME GROUP BY MONTH, FILTER YEAR AND IS PAID
    @Query("SELECT year, month, SUM(paymentAllNights) AS amount FROM payment WHERE year=:year AND isPaid=:isPaid GROUP BY year AND month")
    suspend fun getAllPaymentGroupByMY(year: Int, isPaid: Boolean):List<AmountGroupBy>

    //GET BOOKED DAYS GROUP BY MONTH, FILTER YEAR
    @Query("SELECT year, month, SUM(nights) AS amount FROM payment WHERE flat_id=:flatId AND year=:year GROUP BY year AND month")
    suspend fun getBookedNightsGroupByMY(flatId: Int, year: Int):List<AmountGroupBy>

    //GET BOOKED DAYS GROUP BY MONTH, FILTER YEAR
    @Query("WITH bookedNights (" +
            "SELECT flat_id, year, month, SUM(nights) AS amount " +
            "FROM payment WHERE year=:year GROUP BY flat_id AND year AND month)" +
            "SELECT year, month AVG(amount) AS amount FROM bookedNights GROUP BY year AND month")
    suspend fun getAvgBookedNightsGroupByMY(year: Int):List<AmountGroupBy>

    //ADD EXPENSES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpenses(expenses: Expenses)

    //GET EXPENSES GROUP BY MONTH, FILTER YEAR AND FLAT
    @Query("SELECT year, month, SUM(amount) AS amount FROM expenses WHERE flat_id=:flatId AND year=:year GROUP BY year AND month")
    suspend fun getExpensesGroupByMY(flatId: Int, year: Int):List<AmountGroupBy>

    //GET ALL EXPENSES GROUP BY MONTH, FILTER YEAR
    @Query("SELECT year, month, SUM(amount) AS amount FROM expenses WHERE year=:year GROUP BY year AND month")
    suspend fun getAllExpensesGroupByMY(year: Int):List<AmountGroupBy>

    //ADD/UPDATE CATEGORY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategoryType(categoryType: CategoryType)

    //ADD/UPDATE CATEGORY
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    //GET CATEGORIES BY TYPE ID
    @Query("SELECT * FROM category WHERE type_id=:typeId")
    suspend fun getCategoriesByTypeId(typeId: Int):List<Category>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID
    @Query("SELECT * FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND month=:months")
    fun getExpensesByTypeId(flatId: Int, year: Int, month: Int, typeId: Int):Flow<List<Expenses>>


    //GET INCOME TRANSACTIONS BY PAYMENT DAY
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND isPaid=true GROUP BY month, paymentDate")
    fun getIncomeTransactions(year: Int): Flow<List<TransactionsMonth>>

    //GET INCOME TRANSACTIONS BY PAYMENT DAY, FLAT ID
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id=:flatId AND year=:year AND isPaid=true GROUP BY month, paymentDate")
    fun getIncomeTransactionsFlatId(flatId: Int, year: Int): Flow<List<TransactionsMonth>>

    //GET INCOME TRANSACTIONS BY PAYMENT DAY, MONTH
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND isPaid=true AND month IN (:months) GROUP BY month, paymentDate")
    fun getIncomeTransactionsMonth(year: Int, months: List<Int>): Flow<List<TransactionsMonth>>

    //GET INCOME TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id=:flatId AND year=:year AND isPaid=true AND month IN (:months) GROUP BY month, paymentDate")
    fun getIncomeTransactionsFlatIdM(flatId: Int, year: Int, months: List<Int>): Flow<List<TransactionsMonth>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY
    @Query("SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses " +
            "WHERE year=:year GROUP BY month, paymentDate")
    fun getExpensesTransactions(year: Int): Flow<List<TransactionsMonth>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, FLAT ID
    @Query("SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year GROUP BY month, paymentDate")
    fun getExpensesTransactionsByFlatId(flatId: Int, year: Int):Flow<List<TransactionsMonth>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH
    @Query("SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND month IN (:months) GROUP BY month, paymentDate")
    fun getExpensesTransactionsMonth(year: Int, months: List<Int>):Flow<List<TransactionsMonth>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID
    @Query("SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND month IN (:months) GROUP BY month, paymentDate")
    fun getExpensesTransactionsByFlatIdM(flatId: Int, year: Int, months: List<Int>):Flow<List<TransactionsMonth>>



    //GET ALL TRANSACTIONS BY PAYMENT DAY
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND isPaid=true GROUP BY month, paymentDate " +
            "UNION ALL " +
            "SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses FROM expenses " +
            "WHERE year=:year GROUP BY month, paymentDate " +
            "ORDER BY month, paymentDate")
    fun getAllTransactions(year: Int):Flow<List<TransactionsMonth>>

    //GET ALL TRANSACTIONS BY PAYMENT DAY, FLAT ID
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id=:flatId AND year=:year AND isPaid=true GROUP BY month, paymentDate " +
            "UNION ALL " +
            "SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year GROUP BY month, paymentDate " +
            "ORDER BY month, paymentDate")
    fun getAllTransactionsByFlatId(flatId: Int, year: Int):Flow<List<TransactionsMonth>>

    //GET ALL TRANSACTIONS BY PAYMENT DAY, MONTH
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND isPaid=true AND month IN (:months) GROUP BY month, paymentDate " +
            "UNION ALL " +
            "SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses FROM expenses " +
            "WHERE year=:year AND month IN (:months) GROUP BY month, paymentDate " +
            "ORDER BY month, paymentDate")
    fun getAllTransactionsMonth(year: Int, months: List<Int>):Flow<List<TransactionsMonth>>

    //GET ALL TRANSACTIONS BY PAYMENT DAY, FLAT ID, MONTH
    @Query("SELECT month, paymentDate, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id=:flatId AND year=:year AND isPaid=true AND month IN (:months) GROUP BY month, paymentDate " +
            "UNION ALL " +
            "SELECT month, paymentDate, SUM(amount)*-1 AS amount, comment FROM expenses FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND month IN (:months) GROUP BY month, paymentDate " +
            "ORDER BY month, paymentDate")
    fun getAllTransactionsByFlatIdM(flatId: Int, year: Int, months: List<Int>):Flow<List<TransactionsMonth>>

}