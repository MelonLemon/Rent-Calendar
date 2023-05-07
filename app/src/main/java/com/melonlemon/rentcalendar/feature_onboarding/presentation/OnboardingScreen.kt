package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    onFinish: () -> Unit,
    viewModel: OnBoardingViewModel
) {

    val onBoardingState by viewModel.onBoardingState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val initialSettings = InitialSettings()
    val settingsInfo = initialSettings.getInitialSettings(context)

    val initSettings = remember { mutableStateOf(false) }
    if(!initSettings.value){
        viewModel.onBoardingScreenEvents(OnBoardingEvents.InitSettings(settingsInfo))
    }
    LaunchedEffect(key1 = true){
        viewModel.onBoardingUiEvents.collectLatest { event ->
            when(event) {
                is OnBoardingUiEvents.ShowErrorMessage -> {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(event.message),
                        actionLabel = null
                    )
                }
                is OnBoardingUiEvents.FinishOnBoarding -> {
                    onFinish()
                }
                is OnBoardingUiEvents.FinishInitSettings -> {
                    initSettings.value = true
                }
            }
        }

    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ){
            HorizontalPager(
                modifier = Modifier.weight(10f).padding(16.dp),
                count = 4,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                if(position==0){
                    WelcomePage()
                }
                if(position==1){
                    IntroduceFlatPage(
                        newFlat = onBoardingState.newFlat,
                        onNameChanged= { name ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNewNameChanged(name))
                        },
                        listBaseFlat=onBoardingState.tempFlats,
                        onNewFlatAdd={
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNewFlatAdd)
                        },
                        onNameFlatChanged = { index, name ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNameFlatChanged(
                                    index=index, name=name
                                ))
                        }
                    )
                }
                if(position==2){
                    ExpCategoriesInfo()
                }
                if(position==3){
                    IntroduceExpCategoriesPage(
                        isMonthCat=onBoardingState.isMonthCat,
                        onSegmentBtnClick={ isMonthCatChosen ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnSegmentBtnClick(isMonthCatChosen))

                        },
                        tempMonthlyExpCat=onBoardingState.tempMonthlyExpCat,
                        tempIrregularExpCat=onBoardingState.tempIrregularExpCat,
                        newNameCat=onBoardingState.newNameCat,
                        newAmountCat=onBoardingState.newAmountCat,
                        onNewAmountChange={ amount ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNewAmountChange(amount))
                        },
                        onNewNameChange={ name ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNewNameChange(name))
                        },
                        onNewCatAdd={
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNewCatAdd)
                        },
                        onNameCatChanged={ index, name ->
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnNameCatChanged(
                                    index=index, name=name
                                ))

                        },
                        onAmountCatChanged={ index, amount ->

                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.OnAmountCatChanged(
                                    index=index, amount=amount.toIntOrNull()?:0
                                ))
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
                visible = pagerState.currentPage==3
            ) {
                SectionButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.add_btn),
                    isSelected = true,
                    onBtnClick = {
                        val monthlyCatNames = onBoardingState.tempMonthlyExpCat.map{it.name}
                        val irCatNames = onBoardingState.tempIrregularExpCat.map{it.name}
                        val flatNoDuplicate =  onBoardingState.tempFlats.size == onBoardingState.tempFlats.distinct().count()
                        val monthlyCatNoDuplicate =  monthlyCatNames.size == monthlyCatNames.distinct().count()
                        val irCatNoDuplicate =  irCatNames.size == irCatNames.distinct().count()

                        if(flatNoDuplicate && monthlyCatNoDuplicate && irCatNoDuplicate){
                            viewModel.onBoardingScreenEvents(OnBoardingEvents.OnSaveBaseOptionClick)
                        } else {
                            viewModel.onBoardingScreenEvents(
                                OnBoardingEvents.SendMessage(
                                    R.string.err_msg_onboarding
                                )
                            )
                        }
                    }
                )
            }

        }
    }


}

