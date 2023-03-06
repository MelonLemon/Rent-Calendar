package com.melonlemon.rentcalendar.feature_transaction.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem


fun LazyListScope.TransactionDay(
    title:String,
    listOfItems: List<TransactionListItem>
){
    item {
        Text(
            text=title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    itemsIndexed(listOfItems){ index, item ->
        TransactionRow(
            textFirstR = item.category,
            textSecondR = item.comment,
            amount = item.amount,
            currencySign = item.currencySign
        )
        if(index<listOfItems.lastIndex){
            Divider(
                color = MaterialTheme.colorScheme.secondaryContainer,
                thickness = 2.dp
            )
        }
    }
}

@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    textFirstR: String = "",
    textSecondR: String = "",
    amount: Int,
    currencySign: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = textFirstR,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Text(
                text = textSecondR,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
        Text(
            text = if (amount > 0) "+$amount$currencySign" else "-$amount$currencySign",
            color = if (amount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1
        )
    }

}