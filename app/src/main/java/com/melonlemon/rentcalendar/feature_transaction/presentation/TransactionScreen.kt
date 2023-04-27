package com.melonlemon.rentcalendar.feature_transaction.presentation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_transaction.presentation.components.SearchFilterWidget
import com.melonlemon.rentcalendar.feature_transaction.presentation.components.transactionDay
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionScreenEvents
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val transFilterState by viewModel.transFilterState.collectAsStateWithLifecycle()
    val transactionsByMonth by viewModel.transactionsByMonth.collectAsStateWithLifecycle()
    val isDownloading by viewModel.isDownloading.collectAsStateWithLifecycle()
    val totalSum = remember{
        if(transactionsByMonth.isNotEmpty()){
            derivedStateOf{
                transactionsByMonth.filter { it.yearMonth.monthValue in transFilterState.chosenMonthsNum }
                    .sumOf { it.amount }}
        } else {
            0
        } }

    Scaffold() {
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
                    years = transFilterState.years,
                    selectedYearId = transFilterState.selectedYearId,
                    onMonthClick = { monthNum ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnMonthClick(monthNum))
                    },
                    onYearClick = { yearId->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnYearClick(
                                yearId
                            ))
                    },
                    onYearMonthClick = { transactionPeriod ->
                        viewModel.transactionScreenEvents(
                            TransactionScreenEvents.OnYearMonthClick(transactionPeriod))
                    }
                )
            }
            if(isDownloading) {
                item{
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

            } else {
                item{
                    Text(text= stringResource(R.string.total_sum) + totalSum)
                }
                transactionsByMonth.forEach { month ->
                    if(month.yearMonth.monthValue in transFilterState.chosenMonthsNum){
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

    }
}

