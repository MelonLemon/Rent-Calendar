package com.melonlemon.rentcalendar.feature_home.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar.CustomCalendar
import com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar.getSelectedDatesList
import java.time.LocalDate
import java.time.temporal.ChronoField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesDialog(
    modifier: Modifier = Modifier,
    name: String,
    paymentDate: LocalDate,
    amount: Int,
    onCancel: () -> Unit,
    onAgree: (Int) -> Unit
) {

    var tempAmount by remember{ mutableStateOf(amount) }
    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            modifier = modifier.padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text= stringResource(R.string.change_fix_amount) + " $paymentDate" + stringResource(R.string.for_category) + " $name",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
                OutlinedTextField(
                    value = if(tempAmount!=0) tempAmount.toString() else "",
                    onValueChange = { amount ->
                        tempAmount =  amount.toIntOrNull()?:0
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = MaterialTheme.typography.titleMedium,
                    prefix = { Text(text="~") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ){
                    FilterButton(
                        text = stringResource(R.string.cancel),
                        isSelected = false,
                        onBtnClick = {
                            onCancel()
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    FilterButton(
                        text = stringResource(R.string.agree),
                        isSelected = true,
                        onBtnClick = {
                            if(tempAmount!=0){
                                onAgree(tempAmount)
                            }
                        }

                    )
                }
            }
        }

    }
}

@Composable
fun AllExpCategoriesDialog(
    modifier: Modifier = Modifier,
    regularCategories: List<ExpensesCategoryInfo>,
    irregularCategories: List<ExpensesCategoryInfo>,
    onCancel: () -> Unit,
    onAgree: (List<ExpensesCategoryInfo>) -> Unit
) {

    var tempRegularCategories by remember{ mutableStateOf(regularCategories) }
    var tempIrregularCategories by remember{ mutableStateOf(irregularCategories) }
    var regCatIndex by remember{ mutableStateOf<List<Int>>(emptyList()) }
    var irregCatIndex by remember{ mutableStateOf<List<Int>>(emptyList()) }

    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            modifier = modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        ){
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(10f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item{
                    Text(
                        text= stringResource(R.string.all_categories),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item{
                    Row(){
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint=MaterialTheme.colorScheme.primary)
                        Text(
                            text= stringResource(R.string.regular_btn),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                itemsIndexed(tempRegularCategories){ index, category ->
                    CategoryListItem(
                        name = category.name,
                        amount = category.amount,
                        onAmountChange = { amount ->
                            val newList = tempRegularCategories.toMutableList()
                            val newChangeCategory = newList[index].copy(
                                amount = amount.toIntOrNull() ?: 0
                            )
                            newList[index] = newChangeCategory
                            tempRegularCategories = newList
                            val newRegCatIndex = regCatIndex.toMutableList()
                            if(index !in newRegCatIndex){
                                newRegCatIndex.add(index)
                                regCatIndex = newRegCatIndex
                            }

                        },
                        onNameChange = { name ->
                            val newList = tempRegularCategories.toMutableList()
                            val newChangeCategory = newList[index].copy(
                                name = name
                            )
                            newList[index] = newChangeCategory
                            tempRegularCategories = newList
                            val newRegCatIndex = regCatIndex.toMutableList()
                            if(index !in newRegCatIndex){
                                newRegCatIndex.add(index)
                                regCatIndex = newRegCatIndex
                            }

                        },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item{
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text= stringResource(R.string.irregular_btn),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                itemsIndexed(tempIrregularCategories){ index, category ->
                    CategoryListItem(
                        name = category.name,
                        amount = category.amount,
                        onAmountChange = { amount ->
                            val newList = tempIrregularCategories.toMutableList()
                            newList[index] = newList[index].copy(
                                amount = amount.toIntOrNull() ?: 0
                            )
                            tempIrregularCategories = newList
                            val newIrregCatIndex = irregCatIndex.toMutableList()
                            if(index !in newIrregCatIndex){
                                newIrregCatIndex.add(index)
                                irregCatIndex = newIrregCatIndex
                            }
                        },
                        onNameChange = { name ->
                            val newList = tempIrregularCategories.toMutableList()
                            newList[index] = newList[index].copy(
                                name = name
                            )
                            tempIrregularCategories = newList
                            val newIrregCatIndex = irregCatIndex.toMutableList()
                            if(index !in newIrregCatIndex){
                                newIrregCatIndex.add(index)
                                irregCatIndex = newIrregCatIndex
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ){
                FilterButton(
                    text = stringResource(R.string.cancel),
                    isSelected = false,
                    onBtnClick = {
                        onCancel()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                FilterButton(
                    text = stringResource(R.string.agree),
                    isSelected = true,
                    onBtnClick = {
                        val listRegCat = tempRegularCategories.filterIndexed { index, _ ->
                            index in regCatIndex
                        }
                        val listIrregCat = tempIrregularCategories.filterIndexed { index, _ ->
                            index in irregCatIndex
                        }
                        if(listRegCat.none { it.name.isBlank() } && listIrregCat.none { it.name.isBlank() }){
                            onAgree(listRegCat+listIrregCat)
                        }
                    }

                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    name: String,
    amount: Int,
    onAmountChange: (String) -> Unit,
    onNameChange: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        OutlinedTextField(
            modifier = Modifier.weight(2f),
            value = name,
            onValueChange = onNameChange,
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = if(name.isBlank()) MaterialTheme.colorScheme.error
                else  MaterialTheme.colorScheme.outlineVariant
            ),
            supportingText = {if(name.isBlank()) {
                Text(
                    text = stringResource(R.string.err_msg_empty),
                    overflow = TextOverflow.Visible
                )
            }}
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = if(amount!=0) amount.toString() else "",
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            supportingText = {if(name.isBlank()) {
                Text(
                    text = "",
                    overflow = TextOverflow.Visible
                )
            }},
            placeholder = { Text(text= stringResource(R.string.number_placeholder))}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendarDialog(
    modifier: Modifier = Modifier,
    flatName: String,
    startDate: LocalDate?=null,
    endDate: LocalDate?=null,
    cellSize: Size,
    onCancel: () -> Unit,
    year: Int = LocalDate.now().year,
    onYearChanged: (Int) -> Unit,
    bookedDays: Map<Int, List<LocalDate>>?=null,
    onSave: (startDate: LocalDate?, endDate: LocalDate?) -> Unit
) {

    var tempStartDate by remember{ mutableStateOf(startDate) }
    var tempEndDate by remember{ mutableStateOf(endDate) }
    var selectedWeeks by remember{ mutableStateOf(getSelectedDatesList(startDate=startDate, endDate=endDate)) }

    println("selectedWeeks: $selectedWeeks")
    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(11.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.padding(top=16.dp).weight(1f),
                text=flatName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            CustomCalendar(
                modifier = Modifier.weight(10f),
                tempStartDate=tempStartDate,
                tempEndDate=tempEndDate,
                cellSize=cellSize,
                onDayClick = { date ->
                    if(tempStartDate==null || tempEndDate != null){
                        tempStartDate = date
                        selectedWeeks = getSelectedDatesList(startDate = tempStartDate, endDate = null)
                    } else {
                        val newTempStartDate = if(date.isBefore(tempStartDate)) date else tempStartDate
                        val newTempEndDate = if(date.isAfter(tempStartDate)) date else tempStartDate
                        if(bookedDays==null){
                            tempStartDate = newTempStartDate
                            tempEndDate = newTempEndDate
                            selectedWeeks = getSelectedDatesList(startDate = tempStartDate, endDate = tempEndDate)
                        } else {
                            val newStartWeekNumber = newTempStartDate!!.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
                            val newEndWeekNumber = newTempEndDate!!.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
                            var isOverlap = false
                            (newStartWeekNumber..newEndWeekNumber).forEach{ week->

                                if(bookedDays.containsKey(week)){
                                    run breaking@ {
                                        bookedDays[week]?.forEach { day ->
                                            if(day in newTempStartDate..newTempEndDate){
                                                isOverlap = true
                                                return@breaking
                                            }
                                        }
                                    }
                                }
                            }

                            tempStartDate = if(isOverlap) date else  newTempStartDate
                            tempEndDate = if(isOverlap) null else newTempEndDate
                            selectedWeeks = getSelectedDatesList(startDate = tempStartDate, endDate = tempEndDate)
                        }
                    }

                },
                bookedDays = bookedDays,
                year = year,
                selectedDays = selectedWeeks,
                onYearChanged=onYearChanged
            )
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    onClick = { onCancel()}
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    onClick = { onSave(tempStartDate, tempEndDate)}
                ) {
                    Text(
                        text = stringResource(R.string.agree),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }




    }
}

