package com.melonlemon.rentcalendar.feature_home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenEvents
import com.melonlemon.rentcalendar.feature_onboarding.presentation.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel
) {

    val showErrorMessage by viewModel.showErrorMessage.collectAsStateWithLifecycle()
    val finishOnboarding by viewModel.finishOnboarding.collectAsStateWithLifecycle()

    val listBaseFlat = listOf(stringResource(R.string.main_flat))
    val listMonthlyExpCat = listOf(
        ExpensesCategoryInfo(id=-1, name= stringResource(R.string.monthly_cat_housing), subHeader = "", amount = 0),
        ExpensesCategoryInfo(id=-1, name= stringResource(R.string.internet), subHeader = "", amount = 0),
    )
    val listIrregExpCat = listOf(
        ExpensesCategoryInfo(id=-1, name= stringResource(R.string.cleaning), subHeader = "", amount = 0),
        ExpensesCategoryInfo(id=-1, name= stringResource(R.string.exp_cat_disposable), subHeader = "", amount = 0),
        ExpensesCategoryInfo(id=-1, name= stringResource(R.string.exp_cat_renovation), subHeader = "", amount = 0),
    )

    val pagerState = rememberPagerState()
    var newFlat by remember {  mutableStateOf("") }
    var tempFlats by remember {  mutableStateOf(listBaseFlat) }
    var isMonthCat by remember {  mutableStateOf(true) }
    var newNameCat by remember {  mutableStateOf("") }
    var newAmountCat by remember {  mutableStateOf(0) }
    var tempMonthlyExpCat by remember {  mutableStateOf(listMonthlyExpCat) }
    var tempIrregExpCat by remember {  mutableStateOf(listIrregExpCat) }


    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(R.string.err_msg_onboarding)

    if(showErrorMessage){

        LaunchedEffect(showErrorMessage){

            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = null
            )

        }
    }

    if(finishOnboarding){

        LaunchedEffect(finishOnboarding){

            onFinish()

        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ){
            HorizontalPager(
                modifier = Modifier.weight(10f),
                count = 3,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                if(position==0){
                    WelcomePage()
                }
                if(position==1){
                    IntroduceFlatPage(
                        newFlat = newFlat,
                        onNameChanged= { name ->
                            newFlat=name
                        },
                        listBaseFlat=tempFlats,
                        onNewFlatAdd={
                                if(newFlat.trim().lowercase() !in tempFlats.map{it.lowercase()}){
                                val newList = tempFlats.toMutableList()
                                newList.add(newFlat.trim())
                                tempFlats = newList
                                newFlat = "" }
                        },
                        onNameFlatChanged = { index, name ->
                            val newList = tempFlats.toMutableList()
                            newList[index] = name
                            tempFlats = newList
                        }
                    )
                }
                if(position==2){
                    IntroduceExpCategoriesPage(
                        isMonthCat=isMonthCat,
                        onSegmentBtnClick={ isMonthCatChosen ->
                            isMonthCat = isMonthCatChosen
                        },
                        tempMonthlyExpCat=tempMonthlyExpCat,
                        tempIrregExpCat=tempIrregExpCat,
                        newNameCat=newNameCat,
                        newAmountCat=newAmountCat,
                        onNewAmountChange={ amount ->
                            newAmountCat = amount
                        },
                        onNewNameChange={ name ->
                            newNameCat = name
                        },
                        onNewCatAdd={
                            if(isMonthCat) {
                                if(newNameCat.trim().lowercase() !in tempMonthlyExpCat.map{ it.name.lowercase()}){
                                    val newList = tempMonthlyExpCat.toMutableList()
                                    newList.add(ExpensesCategoryInfo(
                                        id = -1,
                                        name = newNameCat.trim(),
                                        subHeader = "",
                                        amount = newAmountCat
                                    ))
                                    tempMonthlyExpCat = newList
                                    newNameCat = ""
                                    newAmountCat = 0
                                }
                            } else {
                                if(newNameCat.trim().lowercase() !in tempIrregExpCat.map{ it.name.lowercase()}){
                                    val newList = tempIrregExpCat.toMutableList()
                                    newList.add(ExpensesCategoryInfo(
                                        id = -1,
                                        name = newNameCat.trim(),
                                        subHeader = "",
                                        amount = newAmountCat
                                    ))
                                    tempIrregExpCat = newList
                                    newNameCat = ""
                                    newAmountCat = 0
                                }
                            }
                        },
                        onNameCatChanged={ index, name ->
                            if(isMonthCat) {
                                val newList = tempMonthlyExpCat.toMutableList()
                                newList[index] = newList[index].copy(
                                    name = name
                                )
                                tempMonthlyExpCat = newList
                            } else {
                                val newList = tempIrregExpCat.toMutableList()
                                newList[index] = newList[index].copy(
                                    name = name
                                )
                                tempIrregExpCat = newList
                            }
                        },
                        onAmountCatChanged={ index, amount ->
                            if(isMonthCat) {
                                val newList = tempMonthlyExpCat.toMutableList()
                                newList[index] = newList[index].copy(
                                    amount = amount.toIntOrNull() ?: 0
                                )
                                tempMonthlyExpCat = newList
                            } else {
                                val newList = tempIrregExpCat.toMutableList()
                                newList[index] = newList[index].copy(
                                    amount = amount.toIntOrNull() ?: 0
                                )
                                tempIrregExpCat = newList
                            }
                        }
                    )
                }
            }

            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                pagerState = pagerState
            )

            AnimatedVisibility(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                visible = pagerState.currentPage==2
            ) {
                SectionButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.add_btn),
                    isSelected = true,
                    onBtnClick = {
                        val monthlyCatNames = tempMonthlyExpCat.map{it.name}
                        val IrCatNames = tempIrregExpCat.map{it.name}
                        val flatNoDuplicate =  tempFlats.size == tempFlats.distinct().count()
                        val monthlyCatNoDuplicate =  monthlyCatNames.size == monthlyCatNames.distinct().count()
                        val irCatNoDuplicate =  IrCatNames.size == IrCatNames.distinct().count()
                        if(flatNoDuplicate && monthlyCatNoDuplicate && irCatNoDuplicate){
                            viewModel.onboardingScreenEvents(
                                OnboardingEvents.OnSaveBaseOptionClick(
                                    flats = tempFlats,
                                    monthlyExpCategories = tempMonthlyExpCat,
                                    irregExpCategories = tempIrregExpCat
                                )
                            )
                        } else {

                        }
                    }
                )
            }

        }
    }


}

