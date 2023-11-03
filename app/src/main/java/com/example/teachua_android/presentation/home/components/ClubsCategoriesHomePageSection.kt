package com.example.teachua_android.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teachua_android.presentation.categories.CategoryViewModel

@Composable
fun ClubsCategoriesHomePageSection(
    modifier: Modifier = Modifier ,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    Column {
        TitleText(
            title = "Оберіть напрям гуртків" ,
            subtext = "" ,
            titleFontSize = 28.sp ,
            titleLineHeight = 36.sp ,
            Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(modifier = modifier) {
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
            items(viewModel.categoriesState.categories , key = { category ->
                category.id
            }) { category ->
                ClubsCategoryCard(
                    title = category.name ,
                    icon = Icons.Default.ArrowForward ,
                    description = category.description,
                    onCheckClick = {}
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

        }
    }
}
