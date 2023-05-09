package com.melonlemon.rentcalendar.feature_transaction.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.presentation.components.SFilterButton
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType

@Composable
fun TotalAmountTransactions(
    modifier: Modifier = Modifier,
    amount: Int,
    transactionType: TransactionType,
    onTransactionTypeClick: (TransactionType) -> Unit,
    flats: List<CategoryInfo>,
    selectedFlatsId: List<Int>,
    onFlatsClick: (Int) -> Unit,
    chosenPeriod: TransactionPeriod,
    onYearMonthClick: (TransactionPeriod) -> Unit,
    chosenMonthsNum: List<Int>,
    years: List<CategoryInfo>,
    selectedYearId: Int,
    onYearClick: (Int) -> Unit,
    onMonthClick: (Int) -> Unit,
    currency: String = ""
) {
    var showHideFilter by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    modifier = Modifier
                        .size(56.dp),
                    onClick = { showHideFilter = !showHideFilter },
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_filter_list_24),
                        tint = if(showHideFilter) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Filter" )
                }
                Text(
                    text= stringResource(R.string.total_sum) + " $amount$currency",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SFilterButton(
                        modifier = Modifier.weight(1f),
                        text= stringResource(id = transactionType.name) + " " + stringResource(id = R.string.transactions),
                        isSelected = true
                    )
                    SFilterButton(
                        text="${years.find{it.id==selectedYearId}?.name}",
                        isSelected = true
                    )
                }
                if(chosenPeriod==TransactionPeriod.MonthsPeriod){
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        items(
                            items = chosenMonthsNum,
                            key = { monthNum ->
                                java.time.Month.of(monthNum).name + monthNum
                            }
                        ){monthNum ->
                            SFilterButton(
                                text = java.time.Month.of(monthNum).name,
                                isSelected = true
                            )
                        }
                    }
                }
                if(selectedFlatsId.contains(-1)){
                    SFilterButton(
                        text=stringResource(id = R.string.all_flats),
                        isSelected = true
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        items(
                            items = selectedFlatsId,
                            key = { selectedFlatId ->
                                "FL$selectedFlatId"
                            }
                        ){selectedFlatId ->
                            SFilterButton(
                                text = flats.find { it.id== selectedFlatId}!!.name,
                                isSelected = true
                            )
                        }
                    }
                }

            }

            if(showHideFilter){
                Divider(modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                FilterWidget(
                    transactionType=transactionType,
                    onTransactionTypeClick=onTransactionTypeClick,
                    flats=flats,
                    selectedFlatsId=selectedFlatsId,
                    onFlatsClick=onFlatsClick,
                    chosenPeriod=chosenPeriod,
                    onYearMonthClick=onYearMonthClick,
                    chosenMonthsNum=chosenMonthsNum,
                    years=years,
                    selectedYearId=selectedYearId,
                    onYearClick=onYearClick,
                    onMonthClick=onMonthClick)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TotalAmountTransactionsPreview() {
//    RentCalendarTheme {
//        TotalAmountTransactions(
//            amount = 100,
//            filterList = listOf(
//                "All Transactions", "All Flats", "Year: 2023"
//            )
//        )
//    }
//}