package com.karina.carawicara.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.BottomNavBar
import com.karina.carawicara.ui.screen.auth.AuthViewModel
import com.karina.carawicara.ui.screen.auth.AuthViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            application = LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(8.dp),
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_color),
                        contentDescription = "CaraWicara Logo",
                        modifier = Modifier
                            .height(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        ) {
            // Profile section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Patient Avatar",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // User email and role
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = currentUser?.email ?: "user@example.com",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Terapis Wicara",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLogoutDialog = true }
                    .padding(vertical = 8.dp)
            ) {
                // Logout icon placeholder (X in a box)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Logout",
                    tint = Color.Black
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            authViewModel.logout()
                            navController.navigate("loginPage") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        showLogoutDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}