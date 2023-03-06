package com.melonlemon.rentcalendar.feature_analytics.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.data.repository.AnalyticsRepositoryImpl
import com.melonlemon.rentcalendar.core.data.repository.TransactionRepositoryImpl
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.GetCashFlowInfo
import com.melonlemon.rentcalendar.feature_analytics.presentation.components.CompareVariant
import com.melonlemon.rentcalendar.feature_analytics.presentation.components.FinSnapShotContainer
import com.melonlemon.rentcalendar.feature_analytics.presentation.components.PaybackVariant
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.AnalyticsScreenEvents
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.Reports
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenEvents
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetFilteredTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.TransactionsUseCases
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionScreen
import com.melonlemon.rentcalendar.feature_transaction.presentation.TransactionViewModel
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel
) {
    val analyticsFilterState by viewModel.analyticsFilterState.collectAsStateWithLifecycle()
    val finSnapshotState by viewModel.finSnapshotState.collectAsStateWithLifecycle()

    Scaffold() { it ->
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
                            text = stringResource(R.string.report_fin_snapshot),
                            isSelected = analyticsFilterState.chosenReport == Reports.FinSnapShot,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.FinSnapShot))
                            },
                        )
                    }
                    item{
                        FilterButton(
                            text = stringResource(R.string.report_income_st),
                            isSelected = analyticsFilterState.chosenReport == Reports.IncomeStatement,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.IncomeStatement))
                            },
                        )
                    }

                    item{
                        FilterButton(
                            text = stringResource(R.string.report_cash_flow),
                            isSelected = analyticsFilterState.chosenReport == Reports.CashFlow,
                            onBtnClick = {
                                viewModel.analyticsScreenEvents(
                                    AnalyticsScreenEvents.OnReportChange(Reports.CashFlow))
                            },
                        )
                    }
                }
            }
            if(analyticsFilterState.chosenReport == Reports.FinSnapShot){
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
                                    onSecondVChange = { },
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
                                    onFirstVChange = { },
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
                                    onFirstVChange = { },
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
                                    onFirstVChange = { },
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    RentCalendarTheme {
        val repository = AnalyticsRepositoryImpl()
        val useCases = AnalyticsUseCases(
            getCashFlowInfo = GetCashFlowInfo(repository)
        )
        val viewModel = AnalyticsViewModel(useCases)
        AnalyticsScreen(viewModel)
    }
}