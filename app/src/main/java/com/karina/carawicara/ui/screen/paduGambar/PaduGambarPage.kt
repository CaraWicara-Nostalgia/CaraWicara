package com.karina.carawicara.ui.screen.paduGambar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonImage
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.PopupOverview
import com.karina.carawicara.ui.component.ImageSound
import com.karina.carawicara.ui.component.StageBox

data class Soal(
    val gambarUtama: Int,
    val pilihan: List<GambarPilihan>
)

data class GambarPilihan(
    val gambar: Int,
    val nama: String,
    val benar: Boolean
)

@Composable
fun PaduGambarPage(
    navController: NavHostController
) {
    val soalList = listOf(
        Soal(
            gambarUtama = R.drawable.kucing_2,
            pilihan = listOf(
                GambarPilihan(R.drawable.kucing, "Kucing", benar = true),
                GambarPilihan(R.drawable.anjing_3, "Anjing", benar = false),
                GambarPilihan(R.drawable.kuda, "Kuda", benar = false),
                GambarPilihan(R.drawable.kelinci, "Kelinci", benar = false)
            )
        ),
        Soal(
            gambarUtama = R.drawable.anjing_2,
            pilihan = listOf(
                GambarPilihan(R.drawable.kelinci, "Kelinci", benar = false),
                GambarPilihan(R.drawable.anjing_3, "Anjing", benar = true),
                GambarPilihan(R.drawable.kuda, "Kuda", benar = false),
                GambarPilihan(R.drawable.kucing, "Kucing", benar = false)
            )
        ),
        Soal(
            gambarUtama = R.drawable.sapi_3,
            pilihan = listOf(
                GambarPilihan(R.drawable.kucing, "Kucing", benar = false),
                GambarPilihan(R.drawable.ikan, "Ikan", benar = false),
                GambarPilihan(R.drawable.tupai, "Tupai", benar = false),
                GambarPilihan(R.drawable.sapi_2, "Sapi", benar = true)
            )
        ),
        Soal(
            gambarUtama = R.drawable.ikan_2,
            pilihan = listOf(
                GambarPilihan(R.drawable.sapi, "Sapi", benar = false),
                GambarPilihan(R.drawable.anjing_3, "Anjing", benar = false),
                GambarPilihan(R.drawable.ikan, "Ikan", benar = true),
                GambarPilihan(R.drawable.kelinci, "Kelinci", benar = false)
            )
        )
    )

    val currentSoalIndex = remember { mutableStateOf(0) }
    val isPopupVisible = remember { mutableStateOf(false) }
    val isCorrectAnswer = remember { mutableStateOf(false) }
    val stageBoxStatus = remember { mutableStateOf(0) }
    val isEndPopupVisible = remember { mutableStateOf(false) }

    fun validateAnswer(isCorrect: Boolean) {
        isCorrectAnswer.value = isCorrect
        isPopupVisible.value = true
    }

    fun nextSoal() {
        if (currentSoalIndex.value < soalList.size - 1) {
            currentSoalIndex.value += 1
            stageBoxStatus.value += 1
        } else {
            isEndPopupVisible.value = true
        }
    }

    val currentSoal = soalList[currentSoalIndex.value]

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(31.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ButtonNav(
                    onClick = { navController.popBackStack() },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    for (i in 0 until 4) {
                        StageBox(stage = if (i < stageBoxStatus.value) 1 else 0, onClick = { /* Handle click here */ })
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            ImageSound(
                onClick = { /*TODO*/ },
                image = currentSoal.gambarUtama
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    for (pilihan in currentSoal.pilihan.chunked(2)[0]) {
                        ButtonImage(
                            onClick = { validateAnswer(pilihan.benar) },
                            image = pilihan.gambar,
                            text = pilihan.nama,
                            border = Color.Gray.toArgb(),
                            background = Color.White.toArgb()
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    for (pilihan in currentSoal.pilihan.chunked(2)[1]) {
                        ButtonImage(
                            onClick = { validateAnswer(pilihan.benar) },
                            image = pilihan.gambar,
                            text = pilihan.nama,
                            border = Color.Gray.toArgb(),
                            background = Color.White.toArgb()
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            if (isPopupVisible.value) {
                Dialog(
                    onDismissRequest = { isPopupVisible.value = false }
                ) {
                    PopupOverview(
                        onClick = {
                            isPopupVisible.value = false
                            if (isCorrectAnswer.value) {
                                nextSoal()
                            }
                        },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = if (isCorrectAnswer.value) "Yey, kamu berhasil menjawab dengan benar" else "Yah, kamu belum berhasil menjawab dengan benar",
                        image = if (isCorrectAnswer.value) R.drawable.boy_2 else R.drawable.boy_4,
                        message = if (isCorrectAnswer.value) "Lanjutkan" else "Coba lagi"
                    )
                }
            }

            if (isEndPopupVisible.value) {
                Dialog(
                    onDismissRequest = { isEndPopupVisible.value = false }
                ) {
                    PopupOverview(
                        onClick = {
                            isEndPopupVisible.value = false
                            navController.popBackStack() // Kembali ke layar sebelumnya
                        },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = "Yey, kamu telah menyelesaikan semua soal dengan baik!",
                        image = R.drawable.boy_2,
                        message = "Selesai"
                    )
                }
            }
        }
    }
}
