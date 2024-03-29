package com.melonlemon.rentcalendar.feature_transaction.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem
import java.time.Month
import java.time.format.TextStyle
import java.util.*


fun LazyListScope.transactionDay(
    title:String,
    listOfItems: List<TransactionListItem>
){
    item {
        Text(
            text=title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    itemsIndexed(
        items = listOfItems,
        key =  { index, item ->
            "title"+index+item.id
        }
    ){ index, item ->
        val category = if(item.amount>0) stringResource(R.string.rent) else item.category
        val comment = if(item.amount>0) item.comment + " " + stringResource(R.string.days) else
            Month.of(item.comment.toInt()).getDisplayName(TextStyle.FULL, Locale.UK)
        TransactionRow(
            textFirstR = category,
            textSecondR = comment,
            amount = item.amount,
            currencySign = item.currencySign
        )
        if(index<listOfItems.lastIndex){
            Divider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
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
            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .aspectRatio(1f)
                .background(
                    color = if (amount > 0) MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    else MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center

        ){
            Icon(
                imageVector = if(amount > 0) Icons.Filled.Home
                else Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = if(amount > 0)  MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSecondaryContainer
            )

        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = textFirstR,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = textSecondR,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = if (amount > 0) "+$amount$currencySign" else "$amount$currencySign",
            color = if (amount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }

}