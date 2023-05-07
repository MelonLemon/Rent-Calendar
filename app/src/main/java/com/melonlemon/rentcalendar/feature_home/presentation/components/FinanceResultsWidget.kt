package com.melonlemon.rentcalendar.feature_home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.melonlemon.rentcalendar.R
import org.w3c.dom.Text
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = flatName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${month.year}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                CircleDiagram(
                    percent = bookedPercent,
                    name = stringResource(R.string.booked)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = month.month.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        val finResult = income - expenses
                        Text(
                            text = if(finResult>0) "+${String.format("% d",finResult)}$currencySign"
                            else "$finResult$currencySign",
                            style = MaterialTheme.typography.titleLarge,
                            color = if(finResult>0) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSecondaryContainer,
                            textDecoration = TextDecoration.Underline
                        )
                        Text(
                            text = "+ ${String.format("% d",income)}$currencySign",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "- ${String.format("% d",expenses)}$currencySign",
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
                val diagramColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                Canvas(modifier = Modifier.size(radius * 2f)) {
                    drawArc(
                        color = diagramColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(
                            width = 15.dp.toPx(),
                            cap = StrokeCap.Round,
                        )
                    )
                }

            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(radius * 2f)
            ) {
                val diagramColor = MaterialTheme.colorScheme.primary
                Canvas(modifier = Modifier.size(radius * 2f)) {
                    drawArc(
                        color = diagramColor,
                        startAngle = 270f,
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
