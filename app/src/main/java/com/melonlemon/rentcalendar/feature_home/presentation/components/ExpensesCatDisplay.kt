package com.melonlemon.rentcalendar.feature_home.presentation.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.melonlemon.rentcalendar.core.presentation.components.InfoCardInput
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo


fun LazyListScope.expensesCategory(
    title:String,
    displayExpCategories: List<ExpensesCategoryInfo>,
    onAmountChange: (String, Int) -> Unit,
    onAddButtonClicked: (Int) -> Unit
){
    item {
        Text(
            text=title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    itemsIndexed(displayExpCategories){ index, item ->
        InfoCardInput(
            textFirstR = item.name,
            textSecondR = { Text(text = item.subHeader)},
            onNumberChanged = { amountString ->
                onAmountChange(amountString, index) },
            inputNumber = item.amount.toString(),
            onAddButtonClicked = {
                onAddButtonClicked(index)
            },
            content = { }
        )
    }
}