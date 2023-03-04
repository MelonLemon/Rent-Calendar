package com.melonlemon.rentcalendar.feature_home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun RentCard(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
    periodStart: LocalDate,
    periodEnd: LocalDate,
    amount: Int,
    isPaid: Boolean = false,
    onPaidChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                val pattern = "dd.MM"
                val format = DateTimeFormatter.ofPattern(pattern)
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(
                            if (isPaid) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.secondaryContainer
                        ).padding(horizontal = 16.dp, vertical = 8.dp)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (periodStart.format(format) ?: pattern) + " - " +
                                (periodEnd.format(format) ?: pattern),
                        style = MaterialTheme.typography.titleSmall,
                        color = if(isPaid) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                        maxLines = 1
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Switch(checked = isPaid, onCheckedChange = onPaidChange)
                Text(
                    text = "${ChronoUnit.DAYS.between(periodStart, periodEnd)} "
                            + stringResource(R.string.days) + " / $amount",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicCardIconFalsePreview() {
    RentCalendarTheme {
        RentCard(
            name="Lera",
            description="Will be with husband",
            periodStart=LocalDate.now().minusDays(5),
            periodEnd=LocalDate.now().plusDays(2),
            amount=30000,
            isPaid = false,
            onPaidChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BasicCardIconPreview() {
    RentCalendarTheme {
        RentCard(
            name="Lera",
            description="Will be with husband",
            periodStart=LocalDate.now().minusDays(5),
            periodEnd=LocalDate.now().plusDays(2),
            amount=30000,
            isPaid = true,
            onPaidChange = { }
        )
    }
}