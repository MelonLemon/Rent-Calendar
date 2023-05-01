package com.melonlemon.rentcalendar.core.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.melonlemon.rentcalendar.core.data.util.IRREGULAR_EXP
import com.melonlemon.rentcalendar.core.data.util.REGULAR_EXP
import com.melonlemon.rentcalendar.core.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoQueryTests {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var rentDatabase: RentDatabase
    private lateinit var rentDao: RentDao

    @Before
    fun setUp() {
        rentDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RentDatabase::class.java
        ).allowMainThreadQueries().build()
        rentDao = rentDatabase.rentDao
    }

    //Database initializer
    //PASSED
    @Test
    fun addCategoryType_checkAdding() = runBlocking {
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val outputList = rentDao.getCategoryTypes()

        assertEquals(1, outputList.size)
        assertEquals(categoryTypeId, outputList[0].id)
        assertEquals(true, outputList[0].isRegular)
    }

    //BASIC FLAT CHECK
    //PASSED
    @Test
    fun addFlat_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=null, name=flatName, active = true)
        rentDao.addFlat(flat)
        val outputList = rentDao.getFlats()

        assertEquals(1, outputList.size)
        assertEquals(flatName, outputList[0].name)
        assertEquals(true, outputList[0].active)
    }

    //BASIC PERSON CHECK
    //PASSED
    @Test
    fun addPerson_checkAdding() = runBlocking {
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        rentDao.addPerson(person)
        val outputList = rentDao.getAllTenants()

        assertEquals(1, outputList.size)
        assertEquals(personName, outputList[0].name)
        assertEquals(null, outputList[0].phone)
    }

    //BASIC PAYMENT CHECK
    //PASSED
    @Test
    fun addPayment_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val payment = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=3,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 3000,
            isPaid = false
        )
        rentDao.addPayment(payment)
        val outputList = rentDao.getAllPayments()

        assertEquals(1, outputList.size)
        assertEquals(false, outputList[0].isPaid)
        assertEquals(2023, outputList[0].year)
    }

    //PASSED
    @Test
    fun updatePaymentStatus_checkUpdate() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        rentDao.addPerson(person)
        val payment = Payment(
            id=1,
            flatId = 1,
            year=2023,
            month=4,
            nights=3,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 3000,
            isPaid = false
        )
        rentDao.addPayment(payment)
        val outputList = rentDao.getAllPayments()

        assertEquals(false, outputList[0].isPaid)
        rentDao.updatePaymentStatus(id=1, isPaid = true)
        val outputNewList = rentDao.getAllPayments()
        assertEquals(true, outputNewList[0].isPaid)
    }

    //BASIC SCHEDULE
    //PASSED
    @Test
    fun addSchedule_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=1, name=personName, phone=null)
        rentDao.addPerson(person)
        val payment = Payment(
            id=1,
            flatId = 1,
            year=2023,
            month=4,
            nights=3,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 3000,
            isPaid = false )
        rentDao.addPayment(payment)
        val startDate = LocalDate.of(2023,4, 5)
        val endDate = LocalDate.of(2023,4, 8)
        val schedule = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startDate,
            endDate = endDate,
            personId = 1,
            paymentId = 1,
            comment = "" )
        rentDao.addSchedule(schedule)

        val outputList = rentDao.getAllSchedules()

        println("Start date: ${outputList[0].startDate}")

        assertEquals(1, outputList.size)
        assertEquals(2023, outputList[0].year)
        assertEquals(startDate, outputList[0].startDate)
    }

    //PASSED
    @Test
    fun addSchedules_checkFullAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val payment = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=3,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 3000,
            isPaid = false )

        val startDate = LocalDate.of(2023,4, 5)
        val endDate = LocalDate.of(2023,4, 8)
        val schedule = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startDate,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment),
            schedules = listOf(schedule))

        val outputList = rentDao.getAllSchedules()

        assertEquals(1, outputList.size)
        assertEquals(2023, outputList[0].year)
        assertEquals(startDate, outputList[0].startDate)
    }

    //BASIC CATEGORIES
    //PASSED
    @Test
    fun addCategory_checkAdding() = runBlocking {
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = null,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)

        val outputList = rentDao.getAllCategories()

        assertEquals(1, outputList.size)
        assertEquals(1000, outputList[0].fixedAmount)
        assertEquals(categoryName, outputList[0].name)
    }

    //PASSED
    @Test
    fun updateCategory_checkUpdate() = runBlocking {
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)

        val outputList = rentDao.getAllCategories()

        assertEquals(1, outputList.size)
        assertEquals(1000, outputList[0].fixedAmount)
        assertEquals(categoryName, outputList[0].name)

        val newAmount = 1150
        rentDao.updateCategory(id=1, name=categoryName, amount=newAmount)

        val outputNewList = rentDao.getAllCategories()

        assertEquals(1, outputList.size)
        assertEquals(newAmount, outputNewList[0].fixedAmount)
    }

    //PASSED
    @Test
    fun updateCategories_checkUpdate() = runBlocking {
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val amount = 1000
        val categoryName1 = "Sample Category 1"
        val sampleCategory1 = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName1,
            fixedAmount = amount,
            active = true
        )
        val categoryName2 = "Sample Category 2"
        val sampleCategory2 = Category(
            id = 2,
            typeId = categoryTypeId,
            name = categoryName2,
            fixedAmount = amount,
            active = true
        )
        rentDao.addCategory(sampleCategory1)
        rentDao.addCategory(sampleCategory2)

        val outputList = rentDao.getAllCategories()

        assertEquals(2, outputList.size)
        assertEquals(amount, outputList.filter { it.id==1 }[0].fixedAmount)
        assertEquals(categoryName2, outputList.filter { it.id==2 }[0].name)

        val newAmount = 1150
        val newName = "New Category Name"
        rentDao.updateCategories(
            categories = listOf(
                CategoryShortInfo(id=1, name=categoryName1, amount = newAmount),
                CategoryShortInfo(id=2, name=newName, amount = amount)
            ))

        val outputNewList = rentDao.getAllCategories()


        assertEquals(2, outputList.size)
        assertEquals(newAmount, outputNewList.filter { it.id==1 }[0].fixedAmount)
        assertEquals(newName, outputNewList.filter { it.id==2 }[0].name)
    }

    //PASSED
    @Test
    fun getCategoriesByTypId_checkFilter() = runBlocking {
        val categoryTypeId = REGULAR_EXP
        val categoryTypeId2 = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        val categoryType2 = CategoryType(
            id = categoryTypeId2,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        rentDao.addCategoryType(categoryType2)
        val amount = 1000
        val categoryName1 = "Sample Category 1"
        val sampleCategory1 = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName1,
            fixedAmount = amount,
            active = true
        )
        val categoryName2 = "Sample Category 2"
        val sampleCategory2 = Category(
            id = 2,
            typeId = categoryTypeId2,
            name = categoryName2,
            fixedAmount = amount,
            active = true
        )
        rentDao.addCategory(sampleCategory1)
        rentDao.addCategory(sampleCategory2)

        val outputList = rentDao.getCategoriesByTypeId(typeId = categoryTypeId)

        assertEquals(1, outputList.size)
        assertEquals(categoryName1, outputList[0].name)

    }

    //BASIC EXPENSES
    //PASSED
    @Test
    fun addExpenses_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getAllExpenses()

        assertEquals(1, outputList.size)
        assertEquals(amount, outputList[0].amount)


    }

    //PASSED
    @Test
    fun updateExpenses_checkUpdate() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=1,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getAllExpenses()

        assertEquals(1, outputList.size)
        assertEquals(amount, outputList[0].amount)

        val newAmount = 1150
        rentDao.updateExpenses(id=1, amount=newAmount)

        val outputNewList = rentDao.getAllExpenses()
        assertEquals(1, outputNewList.size)
        assertEquals(newAmount, outputNewList[0].amount)
    }

    //HOME SCREEN
    // PASSED
    @Test
    fun getFullRentInfoByYM_checkFilter() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 11000,
            isPaid = false )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = null,
            paymentAllNights = 7000,
            isPaid = false )

        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        rentDao.getFullRentInfoByYM(year=2023,month=4, flatId = 1).test{
            val list = awaitItem()
            assert(list.size==1)
            assert(list[0].schedule.startDate==startMonth)
            cancel()
        }


    }

    // PASSED
    @Test
    fun getPaymentGroupByMY_checkFilterGrouping() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getPaymentGroupByMY(flatId = 1, year=2023, isPaid = true)
        println("OutputList: $outputList")
        assertEquals(2, outputList.size)
        assertEquals(11000, outputList.filter { it.month==3 }[0].amount)
    }

    // PASSED
    @Test
    fun getAllPaymentGroupByMY_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val flatName2 = "Second"
        val flat2 = Flats(id=2, name=flatName2, active = true)
        rentDao.addFlat(flat2)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )

        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllPaymentGroupByMY(year=2023, isPaid = true)
        println("OutputList: $outputList")
        assertEquals(2, outputList.size)
        assertEquals(11000, outputList.filter { it.month==3 }[0].amount)
    }

    // PASSED
    @Test
    fun getMostIncomeMonth_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))


        val outputList = rentDao.getMostIncomeMonth(flatId = 1, year=2023, isPaid = true)
        println("OutputList: $outputList")
        assertEquals(3, outputList!!.month)
        assertEquals(11000, outputList!!.amount)
    }

    // PASSED
    @Test
    fun getAllMostIncomeMonth_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))


        val outputList = rentDao.getAllMostIncomeMonth(year=2023, isPaid = true)
        println("OutputList: $outputList")
        assertEquals(3, outputList!!.month)
        assertEquals(11000, outputList!!.amount)
    }

    // PASSED
    @Test
    fun getBookedNightsGroupByMY_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getBookedNightsGroupByMY(flatId = 1, year=2023)
        println("OutputList: $outputList")
        assertEquals(2, outputList.size)
        assertEquals(11, outputList.filter { it.month==3 }[0].amount)
    }

    // PASSED
    @Test
    fun getAvgDaysRent_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAvgDaysRent(flatId = 1, year = 2023)
        println("OutputList: $outputList")
        assertEquals(9, outputList)
    }

    // PASSED
    @Test
    fun getAllAvgDaysRent_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllAvgDaysRent(year = 2023)
        println("OutputList: $outputList")
        assertEquals(9, outputList)
    }

    // PASSED
    @Test
    fun getBookedPercentYear_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getBookedPercentYear(flatId = 1, year=2023)
        println("OutputList: $outputList")
        assertEquals(29, outputList)
    }

    // PASSED
    @Test
    fun getAllBookedPercentYear_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllBookedPercentYear(year=2023)
        println("OutputList: $outputList")
        assertEquals(29, outputList)
    }

    // PASSED
    @Test
    fun getMostBookedMonth_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getMostBookedMonth(flatId = 1, year=2023)
        println("OutputList: $outputList")
        assertEquals(3, outputList!!.month)
        assertEquals(35, outputList!!.percent)
    }

    // PASSED
    @Test
    fun getAllMostBookedMonth_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllMostBookedMonth(year=2023)
        println("OutputList: $outputList")
        assertEquals(3, outputList!!.month)
        assertEquals(35, outputList!!.percent)
    }

    // PASSED
    @Test
    fun getAvgBookedNightsGroupByMY_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAvgBookedNightsGroupByMY(year=2023)
        println("OutputList: $outputList")
        assertEquals(11, outputList.filter { it.month==3 }[0].amount)
        assertEquals(7, outputList.filter { it.month==4 }[0].amount)
    }

    @Test
    fun getBookedDaysByWeek_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = startMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getBookedDaysByWeek(year=2023, flatId=1)
        println("OutputList: $outputList")

    }

    //PASSED
    @Test
    fun getExpensesGroupByMY_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getExpensesGroupByMY(flatId = 1, year=2023)

        assertEquals(1, outputList.size)
        assertEquals(amount*2, outputList[0].amount)
    }

    //PASSED
    @Test
    fun getAllExpensesGroupByMY_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getAllExpensesGroupByMY(year=2023)

        assertEquals(1, outputList.size)
        assertEquals(amount*2, outputList[0].amount)
    }


    //TRANSACTIONS SCREEN
    //PASSED
    @Test
    fun getExpensesByTypeId_checkFilter() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        val expenses1 = Expenses(
            id=1,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        val expenses2 = Expenses(
            id=2,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        rentDao.addExpenses(expenses1)
        rentDao.addExpenses(expenses2)

        rentDao.getExpensesByTypeId(flatId=1, year=2023, month=4, typeId=IRREGULAR_EXP).test{
            val list = awaitItem()
            assert(list.size==2)
            assert(list==listOf(expenses1, expenses2))
            cancel()
        }

    }

    //PASSED
    @Test
    fun getIncomeTransactions_checkGrouping() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)

        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))


        rentDao.getIncomeTransactions(year=2023).test{
            val list = awaitItem()
            assert(list.size==1)
//            assert(list==listOf(TransactionsDay(paymentDate = startDate, amount = 18000, comment = "18")))
            cancel()
        }

    }

    //PASSED
    @Test
    fun getIncomeTransactionsFlatId_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)

        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))


//        rentDao.getIncomeTransactionsFlatId(flatId=1, year=2023).test{
//            val list = awaitItem()
//            assert(list.size==1)
////            assert(list==listOf(TransactionsMonth(month=3, paymentDate = startDate, amount = 18000, comment = "18")))
//            cancel()
//        }
    }


    //PASSED
    @Test
    fun getExpensesTransactions_checkGrouping() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        val expenses1 = Expenses(
            id=1,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        val expenses2 = Expenses(
            id=2,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        rentDao.addExpenses(expenses1)
        rentDao.addExpenses(expenses2)

//        rentDao.getExpensesTransactions(year=2023).test{
//            val list = awaitItem()
//            assert(list.size==1)
//            //println("OutputList: $list")
//            assert(list==listOf(TransactionsMonth(month=4, paymentDate = LocalDate.of(2023,4,5), amount = -amount*2, comment = "Sample Category")))
//            cancel()
//        }

    }


    //PASSED
    @Test
    fun getExpensesTransactionsByFlatId_checkGrouping() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        val expenses1 = Expenses(
            id=1,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        val expenses2 = Expenses(
            id=2,
            flatId = 1,
            year=2023,
            month=4,
            categoryId = 1,
            amount=amount,
            paymentDate = LocalDate.of(2023,4,5),
            comment = "Sample Category"
        )
        rentDao.addExpenses(expenses1)
        rentDao.addExpenses(expenses2)

//        rentDao.getExpensesTransactionsByFlatId(flatId=1, year=2023).test{
//            val list = awaitItem()
//            assert(list.size==1)
//            //println("OutputList: $list")
//            assert(list==listOf(TransactionsMonth(month=4, paymentDate = LocalDate.of(2023,4,5), amount = -amount*2, comment = "Sample Category")))
//            cancel()
//        }

    }

    //PASSED
    @Test
    fun getAllTransactions_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )

        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

//        rentDao.getAllTransactions(year=2023).test{
//            val list = awaitItem()
//            println("OutputList: $list")
//            assert(list.size==2)
//            assert(list==listOf(
//                TransactionsMonth(month=3, paymentDate = startDate, amount = 18000, comment = "18"),
//                TransactionsMonth(month=4, paymentDate = LocalDate.of(2023,4,5), amount = -amount*2, comment = "Sample Category")))
//            cancel()
//        }

    }

    //PASSED
    @Test
    fun getAllTransactionsByFlatId_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = REGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )

        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

//        rentDao.getAllTransactionsByFlatId(flatId=1, year=2023).test{
//            val list = awaitItem()
//            println("OutputList: $list")
//            assert(list.size==2)
//            assert(list==listOf(
//                TransactionsDay(month=3, paymentDate = startDate, amount = 18000, comment = "18"),
//                TransactionsDay(month=4, paymentDate = LocalDate.of(2023,4,5), amount = -amount*2, comment = "Sample Category")))
//            cancel()
//        }

    }



    //ANALYSIS SCREEN
    //PASSED
    @Test
    fun getPaymentQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getPaymentQuarter(flatId=1, year=2023, isPaid=true)
        println("OutputList: $outputList")

        assertEquals(2, outputList.size)
        assertEquals(11000, outputList[1])

    }
    @Test
    fun getAllPaymentQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllPaymentQuarter(year=2023, isPaid=true)
        println("OutputList: $outputList")

        assertEquals(2, outputList.size)
        assertEquals(11000, outputList[1])
    }

    //PASSED
    @Test
    fun getExpensesQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getExpensesQuarter(flatId=1, year=2023, typeId=IRREGULAR_EXP)

        assertEquals(1, outputList.size)
        assertEquals(amount*2, outputList[2])
    }

    //PASSED
    @Test
    fun getAllExpensesQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getAllExpensesQuarter(year=2023, typeId=IRREGULAR_EXP)
        println("OutputList: $outputList")
        assertEquals(1, outputList.size)
        assertEquals(amount*2, outputList[2])
    }

    //PASSED
    @Test
    fun getPaymentByDateQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = LocalDate.of(2023,5, 8),
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = LocalDate.of(2023,5, 8),
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getPaymentByDateQuarter(flatId=1, year=2023, isPaid=true)
        println("OutputList: $outputList")

        assertEquals(1, outputList.size)
        assertEquals(18000, outputList[2])
    }

    //PASSED
    @Test
    fun getAllPaymentByDateQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val personName = "Lirma"
        val person = Person(id=null, name=personName, phone=null)
        val startDate = LocalDate.of(2023,3, 20)
        val endMonth = LocalDate.of(2023,3, 31)
        val startMonth = LocalDate.of(2023,4, 1)
        val endDate = LocalDate.of(2023,4, 8)
        val payment1 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            nights=11,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 11000,
            isPaid = true )

        val payment2 = Payment(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            nights=7,
            paymentSingleNight = 1000,
            paymentDate = startDate,
            paymentAllNights = 7000,
            isPaid = true )


        val schedule1 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=3,
            startDate = startDate,
            endDate = endMonth,
            personId = -1,
            paymentId = -1,
            comment = "" )

        val schedule2 = Schedule(
            id=null,
            flatId = 1,
            year=2023,
            month=4,
            startDate = startMonth,
            endDate = endDate,
            personId = -1,
            paymentId = -1,
            comment = "" )

        rentDao.addSchedules(
            person=person,
            payments=listOf(payment1, payment2),
            schedules = listOf(schedule1, schedule2))

        val outputList = rentDao.getAllPaymentByDateQuarter(year=2023, isPaid=true)
        println("OutputList: $outputList")

        assertEquals(1, outputList.size)
        assertEquals(18000, outputList[1])
    }

    //PASSED
    @Test
    fun getExpensesByDateQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getExpensesByDateQuarter(flatId=1, year=2023)
        println("OutputList: $outputList")

        assertEquals(1, outputList.size)
        assertEquals(listOf(DisplayInfo(name=categoryName, amount=2000)), outputList[2])
    }

    //PASSED
    @Test
    fun getAllExpensesByDateQuarter_checkAdding() = runBlocking {
        val flatName = "Central"
        val flat = Flats(id=1, name=flatName, active = true)
        rentDao.addFlat(flat)
        val categoryTypeId = IRREGULAR_EXP
        val categoryType = CategoryType(
            id = categoryTypeId,
            isRegular = true,
        )
        rentDao.addCategoryType(categoryType)
        val categoryName = "Sample Category"
        val sampleCategory = Category(
            id = 1,
            typeId = categoryTypeId,
            name = categoryName,
            fixedAmount = 1000,
            active = true
        )
        rentDao.addCategory(sampleCategory)
        val amount = 1000
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        rentDao.addExpenses(
            Expenses(
                id=null,
                flatId = 1,
                year=2023,
                month=4,
                categoryId = 1,
                amount=amount,
                paymentDate = LocalDate.of(2023,4,5),
                comment = "Sample Category"
            )
        )
        val outputList = rentDao.getAllExpensesByDateQuarter(year=2023)

        assertEquals(1, outputList.size)
        assertEquals(listOf(DisplayInfo(name=categoryName, amount=2000)), outputList[2])

    }


    @After
    fun tearDown() {
        rentDatabase.close()
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}