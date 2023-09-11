package com.example.teachua_android.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachua_android.presentation.ui.theme.OrangePrimary


@Preview(showBackground = true)
@Composable
fun MainHeader(){
    //TODO add location handling
    val city = "Київ"
    Column(
        modifier = Modifier
            //TODO add bg picture
            .background(Color.Gray)
            .fillMaxWidth()
            .padding(12.dp)
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(80.dp))
            LocationFragment(city)
            Button(
                onClick = {
                /*TODO*/
                },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(text = "Додати гурток", color = Color.White)
            }
        }
        Row(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Гуртки у місті $city",
                color = Color.White,
                fontSize = 30.sp
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color.White
            )
        }
        UnderlinedTextInput(
            value = "Який гурток шукаєте?",
            onValueChange = { TODO() }
        )
    }
}

@Composable
fun UnderlinedTextInput(value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 15.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .weight(1f)
                    .background(Color.Transparent),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,

                    ),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),

            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search clubs",
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
    }
}
@Composable
fun LocationFragment(location: String) {
    Row(
        modifier = Modifier
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = location,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Location dropdown arrow",
            tint = Color.White
        )
    }

}

