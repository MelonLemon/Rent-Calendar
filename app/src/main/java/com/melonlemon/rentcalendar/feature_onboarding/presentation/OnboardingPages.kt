package com.melonlemon.rentcalendar.feature_onboarding.presentation

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
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.core.presentation.components.NameInputPlus
import com.melonlemon.rentcalendar.core.presentation.components.NameValueInputPlus
import com.melonlemon.rentcalendar.core.presentation.components.SegmentedTwoBtns
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo

@Composable
fun WelcomePage(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.welcome),
                contentDescription = null)
        }

        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_app),
                style = MaterialTheme.typography.headlineLarge.copy(
                    lineBreak = LineBreak.Heading
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroduceFlatPage(
    modifier: Modifier = Modifier,
    newFlat: String,
    onNameChanged: (String) -> Unit,
    listBaseFlat: List<String>,
    onNewFlatAdd: () -> Unit,
    onNameFlatChanged: (Int, String)  -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ){

        item{
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.building),
                    contentDescription = null)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text= stringResource(R.string.welcome_add_flat),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

        }
        item{
            NameInputPlus(
                name = newFlat,
                onNameChanged = { name ->
                    onNameChanged(name)
                },
                onAddButtonClicked = onNewFlatAdd,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            )
        }

        itemsIndexed(listBaseFlat){ index, flat ->
            val isDuplicate = listBaseFlat.filter { it== flat}.size>1
            OutlinedTextField(
                value = flat,
                onValueChange = { name ->
                    onNameFlatChanged(index, name)

                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = if(isDuplicate) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outlineVariant,
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    textColor = MaterialTheme.colorScheme.onSurface
                ),
                supportingText = {
                    if(isDuplicate){
                        Text(text= stringResource(R.string.duplicate))
                    }},
                placeholder = { Text(text= stringResource(R.string.name)) },
            )
        }
    }
}

@Composable
fun ExpCategoriesInfo(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ){


        item{
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.balance),
                    contentDescription = null)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text= stringResource(R.string.welcome_add_categories),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

        }
        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_categories_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }



    }
}

@Composable
fun IntroduceExpCategoriesPage(
    modifier: Modifier = Modifier,
    isMonthCat: Boolean,
    onSegmentBtnClick: (Boolean) -> Unit,
    tempMonthlyExpCat: List<DisplayInfo>,
    tempIrregularExpCat: List<DisplayInfo>,
    newNameCat: String,
    newAmountCat: Int,
    onNewAmountChange: (Int) -> Unit,
    onNewNameChange: (String) -> Unit,
    onNewCatAdd: () -> Unit,
    onNameCatChanged: (Int, String)  -> Unit,
    onAmountCatChanged: (Int, String)  -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ){

        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_add_exp_cat),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        item {
            SegmentedTwoBtns(
                firstBtnName = stringResource(R.string.regular_btn),
                secondBtnName = stringResource(R.string.irregular_btn),
                isFirstBtnSelected = isMonthCat,
                onBtnClick = { isMonthCatChosen ->
                    onSegmentBtnClick(isMonthCatChosen)
                }
            )
        }

        item {
            NameValueInputPlus(
                name = newNameCat,
                number = newAmountCat,
                onNumberChanged = { amount ->
                    onNewAmountChange(amount.toIntOrNull() ?:0)
                },
                onNameChanged = { name ->
                    onNewNameChange(name)
                },
                onAddButtonClicked = onNewCatAdd,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            )
        }

        itemsIndexed(if(isMonthCat) tempMonthlyExpCat else tempIrregularExpCat){ index, category ->
            val chosenList = if(isMonthCat) tempMonthlyExpCat else tempIrregularExpCat
            val isDuplicate = chosenList.filter { it.name== category.name}.size>1
            InputCategories(
                name=category.name,
                onNameChanged={ name ->
                    onNameCatChanged(index, name)
                },
                isDuplicate = isDuplicate,
                amount=category.amount,
                onAmountChanged={ amount ->
                    onAmountCatChanged(index, amount)
                })
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputCategories(
    modifier: Modifier = Modifier,
    isDuplicate: Boolean = false,
    name: String,
    onNameChanged: (String) -> Unit,
    amount: Int,
    onAmountChanged: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            placeholder = { Text(text= stringResource(R.string.name), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.weight(2f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = if(isDuplicate) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outlineVariant,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                textColor = MaterialTheme.colorScheme.onSurface
            ),
            supportingText = {
                if(isDuplicate){
                    Text(text= stringResource(R.string.duplicate))
                }},

        )
        OutlinedTextField(
            value = if(amount==0) "" else "$amount",
            onValueChange = onAmountChanged,
            placeholder = { Text(text= stringResource(R.string.number_placeholder), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                textColor = MaterialTheme.colorScheme.onSurface
            ),
            supportingText = {
                if(isDuplicate){
                    Text(text= "")
                }}
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun WelcomePagePreview() {
//    RentCalendarTheme {
//        WelcomePage()
//    }
//}