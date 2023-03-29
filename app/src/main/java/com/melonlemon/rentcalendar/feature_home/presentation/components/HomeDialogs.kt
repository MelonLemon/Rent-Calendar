package com.melonlemon.rentcalendar.feature_home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDialog(
    modifier: Modifier = Modifier,
    currencySign: String,
    onCancel: () -> Unit,
    onSave: (String) -> Unit
) {
    var newCurrencySign by remember{ mutableStateOf(currencySign) }
    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text= stringResource(R.string.currency_dialog_title),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
            OutlinedTextField(
                value = newCurrencySign,
                onValueChange = { sign ->
                    newCurrencySign =  sign
                },
                placeholder = { Text(text= stringResource(R.string.currency_sign)) },
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
                    text = stringResource(R.string.save),
                    isSelected = true,
                    onBtnClick = {
                        onSave(newCurrencySign)
                    }

                )
            }
        }
    }
}

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
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text= stringResource(R.string.change_fix_amount) + " $paymentDate" + stringResource(R.string.for_category) + " $name",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
            OutlinedTextField(
                value = if(amount!=0) amount.toString() else "",
                onValueChange = { amount ->
                    tempAmount =  amount.toInt()
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
                        onAgree(tempAmount)
                    }

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item{
                Text(
                    text= stringResource(R.string.all_categories),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
            item{
                Text(
                    text= stringResource(R.string.regular_btn),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }

            itemsIndexed(regularCategories){ index, category ->
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
            }

            item{
                Text(
                    text= stringResource(R.string.irregular_btn),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            }
            itemsIndexed(irregularCategories){ index, category ->
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
            }

            item{
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
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = if(name.isBlank()) MaterialTheme.colorScheme.error
                else  MaterialTheme.colorScheme.outlineVariant
            ),
            supportingText = { Text(text = if(name.isBlank())  stringResource(R.string.err_msg_empty) else "")}
        )
        OutlinedTextField(
            value = if(amount!=0) amount.toString() else "",
            onValueChange = onAmountChange,
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
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyDialogPreview() {
    RentCalendarTheme {
        CurrencyDialog(
            currencySign = "",
            onCancel = { },
            onSave = { }
        )
    }
}