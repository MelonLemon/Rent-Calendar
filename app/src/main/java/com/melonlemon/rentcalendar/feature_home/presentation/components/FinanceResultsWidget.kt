package com.melonlemon.rentcalendar.feature_home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.StandardIconBtn
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceResultWidget(
    modifier: Modifier = Modifier,
    flatName: String,
    month: YearMonth,
    bookedPercent: Float,
    income: Int,
    expenses: Int,
    currencySign: String = ""
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = flatName,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                CircleDiagram(
                    percent = bookedPercent,
                    name = stringResource(R.string.booked)
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${month.month.name} ${month.year}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.width(IntrinsicSize.Min),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        val finResult = income - expenses
                        Text(
                            text = if(finResult>0) "+$finResult$currencySign"
                            else "-$finResult$currencySign",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(finResult>0) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Divider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "+ $income$currencySign",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "- $expenses$currencySign",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun CircleDiagram(
    modifier: Modifier = Modifier,
    percent: Float,
    name: String,
    radius: Dp = 50.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(radius * 2f)
            ) {
                val diagramColor = MaterialTheme.colorScheme.primary
                Canvas(modifier = Modifier.size(radius * 2f)) {
                    drawArc(
                        color = diagramColor,
                        startAngle = 30f,
                        sweepAngle = 360 * percent,
                        useCenter = false,
                        style = Stroke(
                            width = 15.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                    )
                }

            }
            Text(
                text = "${(percent * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun FinanceResultWidgetPreview() {
    RentCalendarTheme {
        FinanceResultWidget(
            flatName=" Central Flat",
            month= YearMonth.now(),
            bookedPercent=0.76f,
            income=30000,
            expenses=5000,
            currencySign = "$"
        )
    }
}