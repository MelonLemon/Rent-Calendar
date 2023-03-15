package com.melonlemon.rentcalendar.feature_transaction.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.core.data.repository.TransactionRepositoryImpl
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetFilteredTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.TransactionsUseCases
import com.melonlemon.rentcalendar.feature_transaction.presentation.components.SearchFilterWidget
import com.melonlemon.rentcalendar.feature_transaction.presentation.components.transactionDay
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionScreenEvents
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val transFilterState by viewModel.transFilterState.collectAsStateWithLifecycle()
    val transactionsByMonth by viewModel.transactionsByMonth.collectAsStateWithLifecycle()


    Scaffold() { it ->
        LazyColumn(
            modifier = Modifier.padding(it)
        ){
            item{
                SearchFilterWidget(
                    searchText = searchText,
                    onCancelClicked = {
                        viewModel.transactionScreenEvents(TransactionScreenEvents.OnCancelClicked)
                    },
                    onSearchTextChanged = { text ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnSearchTextChanged(text))
                    },
                    transactionType = transFilterState.transactionType,
                    onTransactionTypeClick = { transactionType ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnTransactionTypeClick(transactionType))
                    },
                    flats = transFilterState.flats,
                    selectedFlatsId = transFilterState.selectedFlatsId,
                    onFlatsClick = { id ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnFlatsClick(id))
                    },
                    chosenMonthsNum = transFilterState.chosenMonthsNum,
                    chosenPeriod = transFilterState.chosenPeriod,
                    year = transFilterState.year,
                    onMonthClick = { monthNum ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnMonthClick(monthNum))
                    },
                    onYearChange = { yearString ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnYearChange(
                                yearString.toIntOrNull() ?:0
                            ))
                    },
                    onYearMonthClick = { transactionPeriod ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnYearMonthClick(transactionPeriod))
                    }
                )
            }

            transactionsByMonth.forEach { month ->
                val sign = if(month.amount>0) "+" else "-"
                val monthTitle = "${month.yearMonth}: $sign${month.amount}${month.currencySign}"
                item {
                    Text(
                        text= monthTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                val pattern = "dd.MM"
                val format = DateTimeFormatter.ofPattern(pattern)
                month.daysList.forEach { days ->
                    transactionDay(
                        title = days.date.format(format) ?: pattern,
                        listOfItems = days.transactions
                    )
                }
            }

        }

    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    RentCalendarTheme {
//        val repository = TransactionRepositoryImpl()
//        val useCases = TransactionsUseCases(
//            getTransactions = GetTransactions(repository),
//            getFilteredTransactions = GetFilteredTransactions()
//        )
//        val viewModel = TransactionViewModel(useCases)
//        TransactionScreen(viewModel)
//    }
//}