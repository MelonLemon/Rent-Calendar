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
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.ChartItem

@Composable
fun IncomeStatementReport(
    modifier: Modifier=Modifier,
    netIncome: Int,
    revenue: Int,
    monthlyCost: Int,
    irregCost: Int
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
                    title = stringResource(R.string.monthly_exp),
                    valueString = "-$monthlyCost",
                )
                TitleAmountRow(
                    title = stringResource(R.string.irreg_exp),
                    valueString = "-$irregCost",
                )
            }
        }
        val listSegments = listOf(
            ChartItem(name = stringResource(R.string.monthly_exp), value = monthlyCost, color = MaterialTheme.colorScheme.tertiaryContainer),
            ChartItem(name = stringResource(R.string.irreg_exp), value = irregCost, color = MaterialTheme.colorScheme.primary),
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
    mostBookedMonth: String,
    mostBookedMonthPercent: Int,
    mostIncomeMonth: String,
    mostIncomeMonthAmount: Int
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
                text = stringResource(R.string.highest_booked_month) + mostBookedMonth + " - "  + "$mostBookedMonthPercent%",
                style =  MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.highest_income) + mostIncomeMonth + " - "  + "$mostIncomeMonthAmount",
                style =  MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun IncomeStatementReportPreview() {
//    RentCalendarTheme {
//        IncomeStatementReport(
//            netIncome = 700,
//            revenue = 1000,
//            monthlyCost = 200,
//            irregCost = 100
//        )
//    }
//}