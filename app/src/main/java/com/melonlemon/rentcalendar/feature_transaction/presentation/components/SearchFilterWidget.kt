package com.melonlemon.rentcalendar.feature_transaction.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun FilterWidget(
    modifier: Modifier = Modifier,
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
    onMonthClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            item{
                SFilterButton(
                    text = stringResource(TransactionType.AllTransaction.name),
                    isSelected = transactionType == TransactionType.AllTransaction,
                    onBtnClick = { onTransactionTypeClick(TransactionType.AllTransaction) }
                )
            }
            item{
                SFilterButton(
                    text = stringResource(TransactionType.ExpensesTransaction.name),
                    isSelected = transactionType == TransactionType.ExpensesTransaction,
                    onBtnClick = { onTransactionTypeClick(TransactionType.ExpensesTransaction) }
                )
            }
            item{
                SFilterButton(
                    text = stringResource(TransactionType.IncomeTransaction.name),
                    isSelected = transactionType == TransactionType.IncomeTransaction,
                    onBtnClick = { onTransactionTypeClick(TransactionType.IncomeTransaction) }
                )
            }

        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            item{
                SFilterButton(
                    text = stringResource(R.string.all),
                    isSelected = -1 in selectedFlatsId,
                    onBtnClick = { onFlatsClick(-1) }
                )
            }
            itemsIndexed(
                items = flats,
                key = { index, flat ->
                    "FL${flat.id}$index"
                }
            ){ _, flat ->
                SFilterButton(
                    text = flat.name,
                    isSelected = flat.id in selectedFlatsId,
                    onBtnClick = { onFlatsClick(flat.id) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            SFilterButton(
                text = stringResource(R.string.year),
                isSelected = chosenPeriod== TransactionPeriod.YearPeriod,
                onBtnClick = { onYearMonthClick(TransactionPeriod.YearPeriod) }
            )
            SFilterButton(
                text = stringResource(R.string.month),
                isSelected = chosenPeriod== TransactionPeriod.MonthsPeriod,
                onBtnClick = { onYearMonthClick(TransactionPeriod.MonthsPeriod) }
            )
            if(chosenPeriod == TransactionPeriod.MonthsPeriod){
                IconButton(onClick = { onMonthClick(-1)}) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                }
            }

        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){


            items(
                items = years,
                key = { year -> year.id }
            ){ year ->
                SFilterButton(
                    text = year.name,
                    isSelected = year.id==selectedYearId,
                    onBtnClick = { onYearClick(year.id) }
                )

            }
        }
        if(chosenPeriod == TransactionPeriod.MonthsPeriod){
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                val monthList = (1..12).toList()

                items(
                    items = monthList,
                    key = { monthNum ->
                        java.time.Month.of(monthNum).name + monthNum
                    }
                ){monthNum ->
                    SFilterButton(
                        text = java.time.Month.of(monthNum).name,
                        isSelected = monthNum in chosenMonthsNum,
                        onBtnClick = { onMonthClick(monthNum) }
                    )

                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(
    modifier:Modifier = Modifier,
    text: String = "",
    onTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChanged,
        shape = MaterialTheme.shapes.small,
        placeholder = { Text(
            text= stringResource(R.string.search),
            color = MaterialTheme.colorScheme.outline
        ) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        trailingIcon = {
            if(text!=""){
                IconButton(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.surface
                    ),
                    onClick = onCancelClicked
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_cancel_24),
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

//@Preview(showBackground = true)
//@Composable
//fun SearchFilterWidgetPreview() {
//    RentCalendarTheme {
//        SearchFilterWidget(
//            searchText = "",
//            onCancelClicked = { },
//            onSearchTextChanged = { },
//            transactionType = TransactionType.AllTransaction,
//            onTransactionTypeClick = { },
//            flats = listOf(CategoryInfo(1, "Central Flat"),
//                CategoryInfo(2, "Secondary Flat")),
//            selectedFlatsId = listOf(1),
//            onFlatsClick = { },
//            chosenMonthsNum = listOf(1, 3),
//            chosenPeriod = TransactionPeriod.MonthsPeriod,
//            selectedYearId = 2023,
//            onMonthClick = { },
//            onYearClick = { },
//            onYearMonthClick = { }
//        )
//    }
//}