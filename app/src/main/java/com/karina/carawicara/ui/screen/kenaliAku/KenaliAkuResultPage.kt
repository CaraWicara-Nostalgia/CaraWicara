package com.karina.carawicara.ui.screen.kenaliAku

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav

var isUsingFrontCamera = true // Pastikan variabel ini tersedia di skop yang tepat

@Composable
fun KenaliAkuResultPage(
    navHostController: NavHostController,
    backStackEntry: NavBackStackEntry
) {
    val context = LocalContext.current
    val imagePath = remember {
        navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("imagePath")
    }

    val accuracy = backStackEntry.arguments?.getString("accuracy") ?: "0"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        imagePath?.let {
            val bitmap = BitmapFactory.decodeFile(it)

            // Membuat matrix untuk rotasi
            val matrix = Matrix().apply {
                postRotate(90f)
                if (isUsingFrontCamera) {
                    postRotate(180f)
                }
            }

            // Menerapkan rotasi ke bitmap
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            Image(
                bitmap = rotatedBitmap.asImageBitmap(),
                contentDescription = "Hasil Gambar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tingkat Akurasi: $accuracy%",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        ButtonNav(
            onClick = { navHostController.navigate("homePage") },
            icon = R.drawable.ic_arrow_forward,
            iconColor = Color.Black.toArgb(),
            borderColor = Color.Gray.toArgb(),
            backgroundColor = Color.White.toArgb(),
            enabled = true
        )
    }
}
