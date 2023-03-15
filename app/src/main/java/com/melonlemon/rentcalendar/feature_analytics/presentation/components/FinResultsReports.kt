package com.melonlemon.rentcalendar.feature_analytics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_analytics.domain.model.ChartItem
import com.melonlemon.rentcalendar.feature_analytics.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

@Composable
fun IncomeStatementReport(
    modifier: Modifier=Modifier,
    netIncome: Int,
    revenue: Int,
    directCost: Int,
    indirectCost: Int
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = netIncome.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            TitleAmountRow(
                title = stringResource(R.string.revenue),
                valueString = "+$revenue",
                isPos = true
            )
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TitleAmountRow(
                    title = stringResource(R.string.direct_cost),
                    valueString = "-$directCost",
                )
                TitleAmountRow(
                    title = stringResource(R.string.indir_cost),
                    valueString = "-$indirectCost",
                )
            }
        }
        val listSegments = listOf(
            ChartItem(name = stringResource(R.string.direct_cost), value = directCost, color = MaterialTheme.colorScheme.tertiaryContainer),
            ChartItem(name = stringResource(R.string.indir_cost), value = indirectCost, color = MaterialTheme.colorScheme.primary),
            ChartItem(name = stringResource(R.string.net_income), value = netIncome, color = MaterialTheme.colorScheme.primaryContainer),
        )
        if(revenue!=0){
            Spacer(modifier = Modifier.width(16.dp))
            SegmentedBarchart(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight(),
                listSegments = listSegments
            )
        }

    }
}

@Composable
fun CashFlowReport(
    modifier: Modifier=Modifier,
    netCashFlow: Int,
    rent: Int,
    listOfExpenses: List<DisplayInfo>
){
    var expended by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = netCashFlow.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        TitleAmountRow(
            title = stringResource(R.string.revenue),
            valueString = "+$rent",
            isPos = true
        )
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
        val sizeList = listOfExpenses.size
            if(expended){
                repeat(sizeList){ index ->
                    TitleAmountRow(
                        title = listOfExpenses[index].name,
                        valueString = "-${listOfExpenses[index].amount}",
                    )
                }
            } else {
                repeat(if(sizeList<3) sizeList else 3){ index ->
                    TitleAmountRow(
                        title = listOfExpenses[index].name,
                        valueString = "-${listOfExpenses[index].amount}",
                    )
                }
                Text(
                    text=".......",
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
            }
            Button(
                onClick = { expended = !expended },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(imageVector = if(expended) ImageVector.vectorResource(id = R.drawable.ic_baseline_expand_less_24)
                    else ImageVector.vectorResource(id = R.drawable.ic_baseline_expand_more_24), 
                    contentDescription = null)
            }
        }
    }
}

@Composable
fun BookedReport(
    modifier: Modifier=Modifier,
    averageBookedPer: Int,
    averageDayRent: Int,
    bestBookedDays: String,
    bestMonth: String,
    valueBestMonth: String
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "~$averageBookedPer%",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.average_booked),
                style =  MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            TitleAmountRow(
                title = stringResource(R.string.aver_day_rent),
                valueString = "$averageDayRent",
                isPos = true
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.best_booked_days) + bestBookedDays,
                style =  MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.best_month) + bestMonth + " " + valueBestMonth,
                style =  MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun IncomeStatementReportPreview() {
    RentCalendarTheme {
        IncomeStatementReport(
            netIncome = 700,
            revenue = 1000,
            directCost = 200,
            indirectCost = 100
        )
    }
}