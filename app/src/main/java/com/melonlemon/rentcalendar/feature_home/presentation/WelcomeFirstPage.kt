package com.melonlemon.rentcalendar.feature_home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeFirstPage(
    modifier: Modifier = Modifier,
    listBaseFlat: List<String>,
    listMonthlyExpCat: List<ExpensesCategoryInfo>,
    listIrregExpCat: List<ExpensesCategoryInfo>,
    onSave: (
        listBaseFlat: List<String>,
        listMonthlyExpCat:List<ExpensesCategoryInfo>,
        listIrregExpCat:List<ExpensesCategoryInfo>,
    ) -> Unit
) {
    var newFlat by remember {  mutableStateOf("") }
    var tempFlats by remember {  mutableStateOf(listBaseFlat) }
    var isMonthCat by remember {  mutableStateOf(true) }

    var newNameCat by remember {  mutableStateOf("") }
    var newAmountCat by remember {  mutableStateOf(0) }
    var tempMonthlyExpCat by remember {  mutableStateOf(listMonthlyExpCat) }
    var tempIrregExpCat by remember {  mutableStateOf(listIrregExpCat) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ){
        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_app),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        item{
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.welcome),
                contentDescription = null)
        }
        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_add_flat),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        item{
            NameInputPlus(
                name = newFlat,
                onNameChanged = { name ->
                    newFlat = name
                },
                onAddButtonClicked = {
                   if(newFlat.trim().lowercase() !in tempFlats.map{it.lowercase()}){
                       val newList = tempFlats.toMutableList()
                       newList.add(newFlat.trim())
                       tempFlats = newList
                       newFlat = ""
                   }
                }
            )
        }
        itemsIndexed(tempFlats){ index, flat ->
            val isDuplicate = tempFlats.filter { it== flat}.size>1
            OutlinedTextField(
                value = flat,
                onValueChange = { name ->
                    val newList = tempFlats.toMutableList()
                    newList[index] = name
                    tempFlats = newList
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = if(isDuplicate) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outlineVariant
                ),
                supportingText = {Text(
                    text=if(isDuplicate) stringResource(R.string.duplicate) else "")},
                placeholder = { Text(text= stringResource(R.string.name)) },
            )
        }
        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_add_categories),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        item{
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.welcome_categories_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        item {
            SegmentedTwoBtns(
                firstBtnName = stringResource(R.string.regular_btn),
                secondBtnName = stringResource(R.string.irregular_btn),
                isFirstBtnSelected = isMonthCat,
                onBtnClick = { isMonthCatChosen ->
                    isMonthCat = isMonthCatChosen
                }
            )
        }

        item {
            NameValueInputPlus(
                name = newNameCat,
                number = newAmountCat,
                onNumberChanged = { amount ->
                    newAmountCat = amount.toIntOrNull() ?:0
                },
                onNameChanged = { name ->
                    newNameCat = name
                },
                onAddButtonClicked = {
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
                }
            )
        }

        itemsIndexed(if(isMonthCat) tempMonthlyExpCat else tempIrregExpCat){ index, category ->
            val chosenList = if(isMonthCat) tempMonthlyExpCat else tempIrregExpCat
            val isDuplicate = chosenList.filter { it.name== category.name}.size>1
            InputCategories(
                name=category.name,
                onNameChanged={ name ->
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
                isDuplicate = isDuplicate,
                amount=category.amount,
                onAmountChanged={ amount ->
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
                })
        }
        item {
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
                        onSave(
                            tempFlats,
                            tempMonthlyExpCat,
                            tempIrregExpCat
                        )
                    }
                }
            )
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            placeholder = { Text(text= stringResource(R.string.name)) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = if(isDuplicate) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outlineVariant
            ),
            supportingText = {Text(
                text=if(isDuplicate) stringResource(R.string.duplicate) else "")},
        )
        OutlinedTextField(
            value = if(amount==0) "" else "$amount",
            onValueChange = onAmountChanged,
            placeholder = { Text(text= stringResource(R.string.number_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}