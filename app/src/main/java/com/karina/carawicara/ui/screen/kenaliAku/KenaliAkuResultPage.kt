package com.karina.carawicara.ui.screen.kenaliAku

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.karina.carawicara.R

var isUsingFrontCamera = true // Pastikan variabel ini tersedia di skop yang tepat

@Composable
fun KenaliAkuResultPage(
    navHostController: NavHostController,
    backStackEntry: NavBackStackEntry
) {
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
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Kembali",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    navHostController.navigate("homePage")
                }
                .padding(4.dp)
        )
    }
}
