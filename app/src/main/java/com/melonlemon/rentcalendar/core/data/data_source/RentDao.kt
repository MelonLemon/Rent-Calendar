package com.melonlemon.rentcalendar.core.data.data_source

import androidx.room.*
import com.melonlemon.rentcalendar.core.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RentDao {

    //ADD/UPDATE CATEGORY TYPE - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategoryType(categoryType: CategoryType)

    //GET CATEGORY TYPE - !TEST PASSED!
    @Query("SELECT * FROM categoryType")
    suspend fun getCategoryTypes(): List<CategoryType>

    //GET ACTIVE YEARS
    @Query("SELECT DISTINCT year FROM schedule")
    suspend fun getYearsActive():List<Int>

    //ADD/UPDATE FLAT - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFlat(flat: Flats)

    //GET ALL FLATS - !TEST PASSED!
    @Query("SELECT * FROM flats")
    suspend fun getFlats():List<Flats>

    //ADD/UPDATE PERSON - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPerson(person: Person): Long

    //GET ALL TENANTS - !TEST PASSED!
    @Query("SELECT * FROM person")
    suspend fun getAllTenants():List<Person>

    //ADD/UPDATE PAYMENT - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPayment(payment: Payment): Long

    //GET ALL PAYMENT (NO FILTER BY is_paid) - !TEST PASSED!
    @Query("SELECT * FROM payment")
    suspend fun getAllPayments():List<Payment>

    //UPDATE PAYMENT STATUS - !TEST PASSED!
    @Query("UPDATE payment SET is_paid =:isPaid WHERE payment_id=:id")
    suspend fun updatePaymentStatus(id: Int, isPaid: Boolean)



    //ADD/UPDATE SCHEDULE - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(schedule: Schedule)

    //ADD SCHEDULES - !TEST PASSED!
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

    //GET ALL SCHEDULES - !TEST PASSED!
    @Query("SELECT * FROM schedule")
    suspend fun getAllSchedules(): List<Schedule>

    //ADD/UPDATE CATEGORY - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    //GET CATEGORIES BY TYPE ID - !TEST PASSED!
    @Query("SELECT * FROM category")
    suspend fun getAllCategories():List<Category>

    //UPDATE CATEGORIES NAME AND FIX AMOUNT - !TEST PASSED!
    @Query("UPDATE category SET name =:name, fix_amount =:amount WHERE category_id=:id")
    suspend fun updateCategory(id: Int, name: String, amount: Int)

    //GET CATEGORIES BY TYPE ID - !TEST PASSED!
    @Query("SELECT * FROM category WHERE type_id=:typeId")
    suspend fun getCategoriesByTypeId(typeId: Int):List<Category>

    //!TEST PASSED!
    @Transaction
    suspend fun updateCategories(categories: List<CategoryShortInfo>){
        categories.forEach { category ->
            updateCategory(
                id = category.id,
                name = category.name,
                amount = category.amount
            )
        }
    }

    //ADD BASE OPTION
    @Transaction
    suspend fun saveBaseOption(flats: List<Flats>, categories: List<Category>){
        println("Start of save base option")
        flats.forEach { flat ->
            addFlat(flat)
        }
        println("Start of save  categories")
        categories.forEach { category ->
            addCategory(category)
        }
        println("The end of save base option")
    }

    //ADD EXPENSES - !TEST PASSED!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpenses(expenses: Expenses)

    //UPDATE PAYMENT STATUS - !TEST PASSED!
    @Query("UPDATE expenses SET amount =:amount WHERE expenses_id=:id")
    suspend fun updateExpenses(id: Int, amount: Int)

    //GET ALL EXPENSES - !TEST PASSED!
    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expenses>

    //HOME SCREEN

    //GET FULL RENT INFO BY YEAR&MONTH AND FLAT ID - !TEST PASSED!
    @Transaction
    @Query("SELECT * FROM schedule WHERE flat_id=:flatId AND year=:year AND month=:month")
    fun getFullRentInfoByYM(year: Int, month: Int, flatId: Int): Flow<List<FullRentInfo>>

    //GET INCOME GROUP BY MONTH, FILTER YEAR, FLAT AND IS PAID - !TEST PASSED!
    @Query("SELECT year, month, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year AND is_paid=:isPaid " +
            "GROUP BY year, month ORDER BY month ASC")
    suspend fun getPaymentGroupByMY(flatId: Int, year: Int, isPaid: Boolean):List<AmountGroupBy>

    //GET ALL INCOME GROUP BY MONTH, FILTER YEAR AND IS PAID - !TEST PASSED!
    @Query("SELECT year, month, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE year=:year AND is_paid=:isPaid " +
            "GROUP BY year, month ORDER BY month ASC")
    suspend fun getAllPaymentGroupByMY(year: Int, isPaid: Boolean):List<AmountGroupBy>

    //GET MOST INCOME MONTH - !TEST PASSED!
    @Query("WITH incomeMonth AS (SELECT year, month, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE year=:year AND flat_id=:flatId AND is_paid=:isPaid " +
            "GROUP BY year, month) " +
            "SELECT year, month, amount FROM incomeMonth WHERE amount = (SELECT MAX(amount) FROM incomeMonth)")
    suspend fun getMostIncomeMonth(flatId: Int, year: Int, isPaid: Boolean): AmountGroupBy?

    //GET MOST INCOME MONTH - !TEST PASSED!
    @Query("WITH incomeMonth AS (SELECT year, month, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE year=:year AND is_paid=:isPaid " +
            "GROUP BY year, month) " +
            "SELECT year, month, amount FROM incomeMonth WHERE amount = (SELECT MAX(amount) FROM incomeMonth)")
    suspend fun getAllMostIncomeMonth(year: Int, isPaid: Boolean): AmountGroupBy?

    //GET BOOKED DAYS GROUP BY MONTH, FILTER YEAR - !TEST PASSED!
    @Query("SELECT year, month, SUM(nights) AS amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year " +
            "GROUP BY year, month ORDER BY month ASC")
    suspend fun getBookedNightsGroupByMY(flatId: Int, year: Int):List<AmountGroupBy>

    //GET AVERAGE DAY RENT - !TEST PASSED!
    @Query("SELECT AVG(nights) FROM payment WHERE  flat_id=:flatId AND year=:year")
    suspend fun getAvgDaysRent(flatId: Int, year: Int): Int?

    //GET AVERAGE DAY RENT - !TEST PASSED!
    @Query("SELECT AVG(nights) FROM payment WHERE year=:year")
    suspend fun getAllAvgDaysRent(year: Int): Int?

    //GET BOOKED DAYS AVERAGE PERCENT - !TEST PASSED!
    @Query("WITH nightsGrouped AS " +
            "(SELECT year, month, SUM(nights) as amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year " +
            "GROUP BY year, month ORDER BY month ASC), " +
            "daysPercent AS (SELECT (amount*100)/STRFTIME( '%d', date(year ||'-01-01','+'||(month-1)||' month', '+1 month','-1 day')) as percent FROM nightsGrouped) " +
            "SELECT AVG(percent) FROM daysPercent")
    suspend fun getBookedPercentYear(flatId: Int, year: Int):Int?

    //GET BOOKED DAYS AVERAGE PERCENT - !TEST PASSED!
    @Query("WITH nightsGrouped AS " +
            "(SELECT year, month, SUM(nights) as amount " +
            "FROM payment WHERE year=:year " +
            "GROUP BY year, month ORDER BY month ASC), " +
            "daysPercent AS (SELECT (amount*100)/STRFTIME( '%d', date(year ||'-01-01','+'||(month-1)||' month', '+1 month','-1 day')) as percent FROM nightsGrouped) " +
            "SELECT AVG(percent) FROM daysPercent")
    suspend fun getAllBookedPercentYear(year: Int):Int?

    //GET MOST BOOKED MONTH - !TEST PASSED!
    @Query("WITH nightsGrouped AS " +
            "(SELECT year, month, SUM(nights) as amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year " +
            "GROUP BY year, month ORDER BY month ASC), " +
            "daysPercent AS (SELECT month, (amount*100)/STRFTIME( '%d', date(year ||'-01-01','+'||(month-1)||' month', '+1 month','-1 day')) as percent FROM nightsGrouped) " +
            "SELECT month, percent  FROM daysPercent WHERE percent = (SELECT MAX(percent) FROM daysPercent)")
    suspend fun getMostBookedMonth(flatId: Int, year: Int):MostBookedMonthInfo?

    //GET ALL MOST BOOKED MONTH - !TEST PASSED!
    @Query("WITH nightsGrouped AS " +
            "(SELECT year, month, SUM(nights) as amount " +
            "FROM payment WHERE year=:year " +
            "GROUP BY year, month ORDER BY month ASC), " +
            "daysPercent AS (SELECT month, (amount*100)/STRFTIME( '%d', date(year ||'-01-01','+'||(month-1)||' month', '+1 month','-1 day')) as percent FROM nightsGrouped), " +
            "percentGrouped AS (SELECT month, SUM(percent) AS percent " +
            "FROM daysPercent " +
            "GROUP BY month) " +
            "SELECT month, percent  FROM percentGrouped WHERE percent = (SELECT MAX(percent) FROM percentGrouped)")
    suspend fun getAllMostBookedMonth(year: Int):MostBookedMonthInfo?

    //GET BOOKED DAYS GROUP BY MONTH, FILTER YEAR - !TEST PASSED!
    @Query("SELECT b.year AS year, b.month AS month, AVG(b.amount) AS amount " +
            "FROM " +
            "(SELECT flat_id, year, month, SUM(nights) AS amount " +
            "FROM payment WHERE year=:year GROUP BY flat_id, year, month) " +
            "AS b " +
            "GROUP BY year, month ORDER BY month")
    suspend fun getAvgBookedNightsGroupByMY(year: Int):List<AmountGroupBy>

    //    //GET BOOKED DAYS GROUP BY WEEK
    @MapInfo(keyColumn = "weekNum", valueColumn = "start_date")
    @Query("WITH RECURSIVE dates AS " +
            "(SELECT schedule_id, start_date, end_date " +
            "FROM schedule WHERE flat_id=:flatId AND year=:year " +
            "UNION ALL " +
            "SELECT schedule_id, (CAST(strftime('%s', datetime(start_date/1000, 'unixepoch', '+1 days')) AS INTEGER) * 1000)  AS start_date, end_date " +
            "FROM dates " +
            "WHERE (CAST(strftime('%s', datetime(start_date/1000, 'unixepoch', '+1 days')) AS INTEGER) * 1000) BETWEEN start_date AND end_date ), " +
            "vacantDays AS " +
            "(SELECT v.date as date FROM " +
            "(SELECT start_date as date FROM schedule WHERE flat_id=:flatId AND year=:year " +
            "UNION ALL " +
            "SELECT end_date as date FROM schedule WHERE flat_id=:flatId AND year=:year" +
            ") as v  GROUP BY date HAVING COUNT(date) = 1)" +
            "SELECT DISTINCT start_date, (CAST(strftime('%W', datetime(start_date/1000, 'unixepoch')) AS Integer)) AS weekNum FROM dates" +
            " WHERE start_date not in (SELECT date FROM vacantDays) ORDER BY start_date ")
    suspend fun getBookedDaysByWeek(year: Int, flatId: Int): Map<Int, List<LocalDate>>?

    //GET EXPENSES GROUP BY MONTH, FILTER YEAR AND FLAT - !TEST PASSED!
    @Query("SELECT year, month, SUM(amount) AS amount FROM expenses WHERE flat_id=:flatId AND year=:year GROUP BY year, month ORDER BY month ASC")
    suspend fun getExpensesGroupByMY(flatId: Int, year: Int):List<AmountGroupBy>

    //GET ALL EXPENSES GROUP BY MONTH, FILTER YEAR - !TEST PASSED!
    @Query("SELECT year, month, SUM(amount) AS amount FROM expenses WHERE year=:year GROUP BY year, month ORDER BY month ASC")
    suspend fun getAllExpensesGroupByMY(year: Int):List<AmountGroupBy>

    //TRANSACTIONS

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID - !TEST PASSED!
    @Query( "SELECT * FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND month=:month AND category_id IN (SELECT category_id FROM category WHERE type_id=:typeId)")
    fun getExpensesByTypeId(flatId: Int, year: Int, month: Int, typeId: Int):Flow<List<Expenses>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID - !TEST PASSED!
    @Query( "SELECT * FROM expenses " +
            "WHERE year=:year AND month=:month AND category_id IN (SELECT category_id FROM category WHERE type_id=:typeId)")
    fun getAllExpensesByTypeId(year: Int, month: Int, typeId: Int):Flow<List<Expenses>>

    //GET INCOME TRANSACTIONS BY PAYMENT DAY
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, '' AS category, " +
            "SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND is_paid=1 GROUP BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), datetime(paymentDate/1000, 'unixepoch')")
    fun getIncomeTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>

    //GET INCOME TRANSACTIONS BY PAYMENT DAY, FLAT ID - !TEST PASSED!
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, " +
            "'' AS category, SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id IN (:flatId) AND year=:year AND is_paid=1 GROUP BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate")
    fun getIncomeTransactionsFlatId(flatId: List<Int>, year: Int): Flow<Map<Int, List<TransactionsDay>>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY - !TEST PASSED!
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, comment AS category, SUM(amount)*-1 AS amount, " +
            "CAST(month AS VARCHAR) AS comment FROM expenses " +
            "WHERE year=:year GROUP BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate, comment")
    fun getExpensesTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, FLAT ID - !TEST PASSED!
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, comment AS category,  SUM(amount)*-1 AS amount, CAST(month AS VARCHAR) AS comment FROM expenses " +
            "WHERE flat_id IN (:flatId) AND year=:year GROUP BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate, comment")
    fun getExpensesTransactionsByFlatId(flatId: List<Int>, year: Int): Flow<Map<Int, List<TransactionsDay>>>

    //GET ALL TRANSACTIONS BY PAYMENT DAY - !TEST PASSED!
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate," +
            " '' AS category,  SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE year=:year AND is_paid=1 GROUP BY (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)), paymentDate " +
            "UNION ALL " +
            "SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)), paymentDate, comment AS category,  SUM(amount)*-1 AS amount, " +
            "CAST(month AS VARCHAR) AS comment  FROM expenses " +
            "WHERE year=:year GROUP BY month, paymentDate, comment " +
            "ORDER BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate, comment")
    fun getAllTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>

    //GET ALL TRANSACTIONS BY PAYMENT DAY, FLAT ID - !TEST PASSED!
    @MapInfo(keyColumn = "month")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, " +
            "'' AS category,  SUM(paymentAllNights) AS amount, CAST(SUM(nights) AS VARCHAR) AS comment FROM payment " +
            "WHERE flat_id IN (:flatId) AND year=:year AND is_paid=1 GROUP BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate " +
            "UNION ALL " +
            "SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer)) AS month, paymentDate, comment AS category, " +
            "SUM(amount)*-1 AS amount, CAST(month AS VARCHAR) AS comment  FROM expenses " +
            "WHERE flat_id IN (:flatId) AND year=:year GROUP BY month, paymentDate, comment " +
            "ORDER BY CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer), paymentDate")
    fun getAllTransactionsByFlatId(flatId: List<Int>, year: Int): Flow<Map<Int, List<TransactionsDay>>>


    // ANALYSIS

    //GET PAYMENT BY FLAT ID BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT (month + 2) / 3  as quarter, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year AND is_paid=:isPaid " +
            "GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getPaymentQuarter(flatId: Int, year: Int, isPaid: Boolean):Map<Int, Int>

    //GET ALL PAYMENT BY FLAT ID BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT (month + 2) / 3  as quarter, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE year=:year AND is_paid=:isPaid " +
            "GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getAllPaymentQuarter(year: Int, isPaid: Boolean):Map<Int, Int>

    //GET EXPENSES BY TYPE ID BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT  (month + 2) / 3  as quarter, SUM(amount) AS amount FROM expenses " +
            "WHERE flat_id=:flatId AND year=:year AND category_id IN (SELECT category_id FROM category WHERE type_id=:typeId)  GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getExpensesQuarter(flatId: Int, year: Int, typeId: Int):Map<Int, Int>

    //GET ALL EXPENSES BY TYPE ID  BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT (month+2)/3 as quarter, SUM(amount) AS amount FROM expenses " +
            "WHERE year=:year AND category_id IN (SELECT category_id FROM category WHERE type_id=:typeId) GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getAllExpensesQuarter(year: Int, typeId: Int):Map<Int, Int>

    //GET PAYMENT BY DATE BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer) + 2) / 3  as quarter, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE flat_id=:flatId AND year=:year AND is_paid=:isPaid " +
            "GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getPaymentByDateQuarter(flatId: Int, year: Int, isPaid: Boolean):Map<Int, Int>

    //GET ALL PAYMENT BY DATE BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter", valueColumn = "amount")
    @Query("SELECT  (CAST(strftime('%m', datetime(paymentDate/1000, 'unixepoch')) AS Integer) + 2) / 3  as quarter, SUM(paymentAllNights) AS amount " +
            "FROM payment WHERE year=:year AND is_paid=:isPaid " +
            "GROUP BY quarter ORDER BY quarter ASC")
    suspend fun getAllPaymentByDateQuarter(year: Int, isPaid: Boolean):Map<Int, Int>

    //GET EXPENSES BY TYPE ID BY QUARTER - !TEST PASSED!
    @MapInfo(keyColumn = "quarter")
    @Query( "SELECT  (CAST(strftime('%m', datetime(expenses.paymentDate/1000, 'unixepoch')) AS Integer) + 2) / 3  as quarter, category.name as name, SUM(expenses.amount) AS amount FROM expenses " +
            "INNER JOIN category ON expenses.category_id=category.category_id " +
            "WHERE flat_id=:flatId AND year=:year GROUP BY quarter, name ORDER BY quarter ASC")
    suspend fun getExpensesByDateQuarter(flatId: Int, year: Int):Map<Int, List<DisplayInfo>>

    //GET EXPENSES TRANSACTIONS BY PAYMENT DAY, MONTH, FLAT ID - !TEST PASSED!
    @MapInfo(keyColumn = "quarter")
    @Query("SELECT (CAST(strftime('%m', datetime(expenses.paymentDate/1000, 'unixepoch')) AS Integer) + 2) / 3  as quarter, category.name as name, SUM(expenses.amount) AS amount " +
            "FROM expenses " +
            "INNER JOIN category ON expenses.category_id=category.category_id " +
            "WHERE year=:year GROUP BY quarter, name ORDER BY quarter ASC")
    suspend fun getAllExpensesByDateQuarter(year: Int):Map<Int, List<DisplayInfo>>

    // GROSS RENT  YEARLY
    @Query("SELECT SUM(paymentAllNights) AS amount FROM payment WHERE year=:year")
    suspend fun getGrossRentYearly(year: Int):Int?

    // GROSS RENT  YEARLY FILTER BY FLAT ID
    @Query("SELECT SUM(paymentAllNights) AS amount FROM payment WHERE year=:year AND flat_id=:flatId")
    suspend fun getGrossRentYearlyByFlatId(flatId: Int, year: Int):Int?

    // GROSS RENT  MONTHLY
    @Query("SELECT SUM(paymentAllNights) AS amount FROM payment WHERE year=:year AND month=:month")
    suspend fun getGrossRentMonthly(year: Int, month: Int):Int?

    // GROSS RENT  MONTHLY FILTER BY FLAT ID
    @Query("SELECT SUM(paymentAllNights) AS amount FROM payment WHERE year=:year AND flat_id=:flatId AND month=:month")
    suspend fun getGrossRentMonthlyByFlatId(flatId: Int, year: Int, month: Int):Int?

    // EXPENSES YEARLY
    @Query("SELECT SUM(amount) AS amount FROM expenses WHERE year=:year")
    suspend fun getExpensesYearly(year: Int):Int?

    // EXPENSES YEARLY FILTER BY FLAT ID
    @Query("SELECT SUM(amount) AS amount FROM expenses WHERE year=:year AND flat_id=:flatId")
    suspend fun getExpensesYearlyByFlatId(flatId: Int, year: Int):Int?

    // EXPENSES MONTHLY
    @Query("SELECT SUM(amount) AS amount FROM expenses WHERE year=:year AND month=:month")
    suspend fun getExpensesMonthly(year: Int, month: Int):Int?

    // EXPENSES MONTHLY FILTER BY FLAT ID
    @Query("SELECT SUM(amount) AS amount FROM expenses WHERE year=:year AND flat_id=:flatId AND month=:month")
    suspend fun getExpensesMonthlyByFlatId(flatId: Int, year: Int, month: Int):Int?
}