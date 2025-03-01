package com.karina.carawicara.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.karina.carawicara.R

sealed class BottomNavBar (
    val route: String,
    val icon: Int,
    val title: String
){
    object Home : BottomNavBar(
        route = "homePage",
        icon = R.drawable.ic_home,
        title = "Home"
    )

    object Patient : BottomNavBar(
        route = "patientPage",
        icon = R.drawable.ic_patient,
        title = "Patient"
    )

    object Profile : BottomNavBar(
        route = "profilePage",
        icon = R.drawable.ic_profile,
        title = "Profile"
    )
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavBar.Home,
        BottomNavBar.Patient,
        BottomNavBar.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            items.forEach { item ->

                val selected = currentRoute == item.route
                val tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray

                Column (
                    modifier = Modifier
                        .clickable {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = item.title,
                        color = tint,
                        fontSize = 12.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}