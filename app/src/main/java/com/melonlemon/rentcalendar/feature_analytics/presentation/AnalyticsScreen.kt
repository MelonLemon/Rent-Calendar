package com.melonlemon.rentcalendar.feature_analytics.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.feature_analytics.presentation.components.*
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.AnalyticsScreenEvents
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.Reports
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel
) {
    val analyticsFilterState by viewModel.analyticsFilterState.collectAsStateWithLifecycle()
    val chosenReport by viewModel.chosenReport.collectAsStateWithLifecycle()
    val finSnapshotState by viewModel.finSnapshotState.collectAsStateWithLifecycle()
    val listOfFlats by viewModel.listOfFlats.collectAsStateWithLifecycle()
    val incomeStatementState by viewModel.incomeStatementState.collectAsStateWithLifecycle()
    val cashFlowState by viewModel.cashFlowState.collectAsStateWithLifecycle()
    val bookedReportState by viewModel.bookedReportState.collectAsStateWithLifecycle()
    val listOfYears by viewModel.listOfYears.collectAsStateWithLifecycle()



    Scaffold {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{
                LazyRow(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ){
                    item{
                        FilterButton(
                            text = stringResource(R.string.report_income_st),
                            isSelected = chosenReport == Reports.IncomeStatement,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.IncomeStatement))
                            },
                        )
                    }

                    item{
                        FilterButton(
                            text = stringResource(R.string.report_cash_flow),
                            isSelected = chosenReport == Reports.CashFlow,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.CashFlow))
                            },
                        )
                    }
                    item{
                        FilterButton(
                            text = stringResource(R.string.booked_report),
                            isSelected = chosenReport == Reports.BookedReport,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.BookedReport))
                            },
                        )
                    }

                    item{
                        FilterButton(
                            text = stringResource(R.string.invest_return),
                            isSelected = chosenReport == Reports.InvestmentReturn,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.InvestmentReturn))
                            },
                        )
                    }
                }
            }

            item{
                LazyRow(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ){
                    item{
                        FilterButton(
                            text = stringResource(R.string.all),
                            isSelected = analyticsFilterState.selectedFlatId == -1,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnFlatClick(-1))
                            },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    items(
                        items = listOfFlats,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
                        FilterButton(
                            text = item.name,
                            isSelected = analyticsFilterState.selectedFlatId == item.id,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnFlatClick(item.id))
                            },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
            item{
                LazyRow(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ){
                    items(
                        items = listOfYears,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
                        FilterButton(
                            text = item.name,
                            isSelected = analyticsFilterState.selectedYearId == item.id,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnFlatClick(item.id))
                            },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            if(chosenReport == Reports.IncomeStatement){
                item{
                    LazyRow(
                        modifier = Modifier,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ){
                        items(
                            items = incomeStatementState,
                            key = { incomeStatement ->
                                incomeStatement.quarter
                            }
                        ){ incomeStatement ->
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.net_income),
                            ){
                                Text(text = "${incomeStatement.quarter} " + stringResource(R.string.quater))
                                IncomeStatementReport(
                                    netIncome = incomeStatement.netIncome,
                                    revenue = incomeStatement.revenue,
                                    monthlyCost = incomeStatement.monthlyExp,
                                    irregCost = incomeStatement.irregExp
                                )
                            }
                        }
                    }

                }
            }
            if(chosenReport == Reports.CashFlow){
                item{
                    LazyRow(
                        modifier = Modifier,
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ){
                        items(
                            items = cashFlowState,
                            key = { cashFlow ->
                                cashFlow.quarter
                            }
                        ){cashFlow ->
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.cash_flow),
                            ){
                                Text(text = "${cashFlow.quarter} " + stringResource(R.string.quater))
                                CashFlowReport(
                                    netCashFlow = cashFlow.netCashFlow,
                                    rent = cashFlow.rent,
                                    listOfExpenses = cashFlow.expenses
                                )
                            }
                        }
                    }

                }
            }
            if(chosenReport == Reports.BookedReport){
                item{
                    FinSnapShotContainer(
                        modifier = Modifier.fillParentMaxWidth(),
                        title = stringResource(R.string.booked),
                    ){
                        BookedReport(
                            averageBookedPer = bookedReportState.averageBooked,
                            averageDayRent = bookedReportState.averageDayRent,
                            mostBookedMonth = Month.of(bookedReportState.mostBookedMonth).getDisplayName(
                                TextStyle.FULL_STANDALONE, Locale.getDefault()),
                            mostBookedMonthPercent = bookedReportState.mostBookedMonthPercent,
                            mostIncomeMonth = Month.of(bookedReportState.mostIncomeMonth).getDisplayName(
                                TextStyle.FULL_STANDALONE, Locale.getDefault()),
                            mostIncomeMonthAmount = bookedReportState.mostIncomeMonthAmount
                        )
                    }
                }
            }
            if(chosenReport == Reports.InvestmentReturn){
                item{
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                        item{
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.grm),
                            ){
                                PaybackVariant(
                                    result = if(finSnapshotState.totalPurchasePrice!=0 && finSnapshotState.yearlyGrossRent!=0)
                                        "${finSnapshotState.totalPurchasePrice/finSnapshotState.yearlyGrossRent}" + stringResource(R.string.years)
                                    else "0 " + stringResource(R.string.years),
                                    description = stringResource(R.string.grm_description),
                                    firstValue = finSnapshotState.totalPurchasePrice,
                                    onFirstVChange = {  valueString ->
                                        viewModel.analyticsScreenEvents(
                                            AnalyticsScreenEvents.OnTotalPurchaseChange(
                                                valueString.toIntOrNull() ?: 0
                                            )
                                        )
                                    },
                                    secondValue = finSnapshotState.yearlyGrossRent,
                                    nameFirstV = stringResource(R.string.total_purchase_price),
                                    nameSecondV = stringResource(R.string.yearly_gross_rent)
                                )
                            }
                        }
                        item{
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.one_per_rule),
                            ){
                                CompareVariant(
                                    firstResult=(finSnapshotState.totalPurchasePrice*0.01f).toInt(),
                                    firstResTitle= stringResource(R.string.good_gross_rent_monthly),
                                    secondResult=finSnapshotState.monthlyGrossRent,
                                    secondResTitle= stringResource(R.string.your_gross_rent_monthly),
                                    firstValue = finSnapshotState.totalPurchasePrice,
                                    secondValue = 1,
                                    nameFirstV = stringResource(R.string.total_purchase_price)
                                )
                            }
                        }
                        item{
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.cap_rate),
                            ){
                                PaybackVariant(
                                    result = if(finSnapshotState.netOperatingIncomeY!=0 && finSnapshotState.totalPurchasePrice!=0)
                                        "${(finSnapshotState.netOperatingIncomeY/finSnapshotState.totalPurchasePrice)*100}%"
                                    else "0%",
                                    description = stringResource(R.string.cap_rate_desc),
                                    firstValue = finSnapshotState.netOperatingIncomeY,
                                    secondValue = finSnapshotState.totalPurchasePrice,
                                    onSecondVChange = {valueString ->
                                        viewModel.analyticsScreenEvents(
                                            AnalyticsScreenEvents.OnTotalPurchaseChange(
                                                valueString.toIntOrNull() ?: 0
                                            )
                                        )
                                    },
                                    nameFirstV =  stringResource(R.string.noi_year),
                                    nameSecondV = stringResource(R.string.total_purchase_price)
                                )
                            }
                        }
                        item{
                            FinSnapShotContainer(
                                modifier = Modifier.fillParentMaxWidth(),
                                title = stringResource(R.string.fifty_rule),
                            ){
                                CompareVariant(
                                    firstResult=(finSnapshotState.monthlyGrossRent*0.5f).toInt(),
                                    firstResTitle= stringResource(R.string.good_noi),
                                    secondResult=finSnapshotState.netOperatingIncomeM,
                                    secondResTitle= stringResource(R.string.your_noi),
                                    firstValue = finSnapshotState.monthlyGrossRent,
                                    secondValue = 50,
                                    nameFirstV = stringResource(R.string.monthly_gross_rent),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

