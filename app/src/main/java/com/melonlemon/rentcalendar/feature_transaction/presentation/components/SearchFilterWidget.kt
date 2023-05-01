package com.melonlemon.rentcalendar.feature_transaction.presentation.components

import androidx.compose.foundation.BorderStroke
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
import com.melonlemon.rentcalendar.core.presentation.components.SearchFilterButton
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType

@Composable
fun SearchFilterWidget(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
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
    SearchFilterContainer(
        modifier = modifier,
        searchText =  searchText,
        onSearchTextChanged = onSearchTextChanged,
        onCancelClicked = onCancelClicked
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            SearchFilterButton(
                text = stringResource(R.string.all),
                isSelected = transactionType == TransactionType.AllTransaction,
                onBtnClick = { onTransactionTypeClick(TransactionType.AllTransaction) }
            )
            SearchFilterButton(
                text = stringResource(R.string.expenses),
                isSelected = transactionType == TransactionType.ExpensesTransaction,
                onBtnClick = { onTransactionTypeClick(TransactionType.ExpensesTransaction) }
            )
            SearchFilterButton(
                text = stringResource(R.string.income),
                isSelected = transactionType == TransactionType.IncomeTransaction,
                onBtnClick = { onTransactionTypeClick(TransactionType.IncomeTransaction) }
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            item{
                SearchFilterButton(
                    text = stringResource(R.string.all),
                    isSelected = -1 in selectedFlatsId,
                    onBtnClick = { onFlatsClick(-1) }
                )
            }
            itemsIndexed(flats){ _, flat ->
                SearchFilterButton(
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
            SearchFilterButton(
                text = stringResource(R.string.year),
                isSelected = chosenPeriod== TransactionPeriod.YearPeriod,
                onBtnClick = { onYearMonthClick(TransactionPeriod.YearPeriod) }
            )
            SearchFilterButton(
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
                SearchFilterButton(
                    text = year.name,
                    isSelected = year.id==selectedYearId,
                    onBtnClick = { onYearClick(year.id) }
                )

            }
        }
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
                SearchFilterButton(
                    text = java.time.Month.of(monthNum).name,
                    isSelected = monthNum in chosenMonthsNum,
                    onBtnClick = { onMonthClick(monthNum) }
                )

            }
        }
    }
}


@Composable
fun SearchFilterContainer(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    var selected by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            SearchInput(
                text = searchText,
                onTextChanged = onSearchTextChanged,
                onCancelClicked = onCancelClicked
            )
            OutlinedButton(
                modifier = Modifier
                    .size(56.dp),
                onClick = { selected = !selected },
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if(selected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(width = 1.dp,  color = if(selected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_filter_list_24),
                    tint = if(selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Filter" )
            }
        }
        if(selected){
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(
    text: String = "",
    onTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        shape = MaterialTheme.shapes.small,
        placeholder = { Text(
            text= stringResource(R.string.search)
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
            textColor = MaterialTheme.colorScheme.onSurface
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