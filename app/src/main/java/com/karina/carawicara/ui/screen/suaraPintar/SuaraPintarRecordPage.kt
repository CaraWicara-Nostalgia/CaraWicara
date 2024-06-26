package com.karina.carawicara.ui.screen.suaraPintar

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.ButtonNav
import com.karina.carawicara.ui.component.PopupOverview
import com.karina.carawicara.ui.component.StageBox

private const val REQUEST_CODE = 1001

@Composable
fun SuaraPintarRecordPage(
    navHostController: NavHostController
) {
    val backgroundColor = remember { mutableStateOf(Color.White) }
    val sendButtonClicked = remember { mutableStateOf(false) }

    val stageBoxStatus = remember {
        mutableStateOf(0)
    }
//    val context = LocalContext.current
//    val permissionGranted = remember { mutableStateOf(false) }
//    val recording = remember { mutableStateOf(false) }
//    val recordingTime = remember { mutableStateOf(0L) }
//
//    val AUDIO_PERMISSIONS = arrayOf(
//        Manifest.permission.RECORD_AUDIO
//    )
//    val allPermissionsGranted = hasRequiredPermissions(context, AUDIO_PERMISSIONS)
//
//    val isRecording = recording.value
//
//    var audioRecord: AudioRecord? = null
//
//    val data = remember { mutableStateOf(ShortArray(0)) }
//    AudioVisualizationView(audioData = data.value)
//    BarVisualizer(data = data.value)

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(31.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ButtonNav(
                    onClick = { navHostController.popBackStack() },
                    icon = R.drawable.ic_x,
                    iconColor = Color.White.toArgb(),
                    borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                    backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                    enabled = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                repeat(4) {
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFFEEFD4))
                    .border(2.dp, Color(0xFFFEE4B7))
                    .padding(4.dp)
            ) {
                Text(
                    text = "tahan tombol rekam untuk mulai merekam suara",
                    fontSize = 20.sp,
                    color = Color(0xFFFCB028),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
//                CountdownTimer(
//                    durationMillis = 10000,
//                    onTimerFinished = { /* Handle timer finished */ },
//                    onTwoSecondsLeft = { backgroundColor.value = Color.Red }
//                )
//                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.record_spectrum),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nama hewan tadi adalah ...",
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(Color.Red)
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Text(
//                    text = formatElapsedTime(recordingTime.value),
//                    fontSize = 20.sp,
//                    color = Color.White,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
//            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ButtonNav(
                        onClick = { /*TODO*/ },
                        icon = R.drawable.ic_mic,
                        iconColor = Color.White.toArgb(),
                        borderColor = MaterialTheme.colorScheme.errorContainer.toArgb(),
                        backgroundColor = MaterialTheme.colorScheme.error.toArgb(),
                        enabled = true
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = { sendButtonClicked.value = true },
                        Modifier
                            .size(239.dp, 47.dp)
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        content = {
                            Text(
                                text = "Kirim",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    )
                }
            }
            if (sendButtonClicked.value) {
                Dialog(
                    onDismissRequest = { sendButtonClicked.value = false }
                ) {
                    PopupOverview(
                        onClick = {
                            stageBoxStatus.value = 0
                            sendButtonClicked.value = false
                        },
                        border = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                        background = MaterialTheme.colorScheme.primary.toArgb(),
                        text = "Yey, kamu berhasil menjawab dengan benar",
                        image = R.drawable.boy_2,
                        message = "Lanjutkan"
                    )
                }
            }
        }
    }

//    if (!allPermissionsGranted) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            AUDIO_PERMISSIONS,
//            REQUEST_CODE
//        )
//    }
//
//    LaunchedEffect(Unit) {
//        if (isRecording) {
//            while (isRecording) {
//                val buffer = ShortArray(BUFFER_SIZE)
//                val result = audioRecord?.read(buffer, 0, BUFFER_SIZE)
//                result?.let {
//                    data.value = buffer
//                }
//                recordingTime.value += 1000
//                delay(1000)
//            }
//        }
//    }
//
//    AudioVisualizationView(audioData = data.value, modifier = Modifier.fillMaxSize())
}

fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(permission),
        requestCode
    )
}

private const val SAMPLE_RATE = 44100
private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

private var audioRecord: AudioRecord? = null

fun startRecording(context: Context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
        // Permission is not granted
        // You can request the permission here or notify the user that the permission is necessary
        return
    }

    audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        SAMPLE_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT,
        BUFFER_SIZE
    )

    val buffer = ShortArray(BUFFER_SIZE)

    audioRecord?.startRecording()

    // Start processing the audio data
    while (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
        val result = audioRecord?.read(buffer, 0, BUFFER_SIZE)
        // Process the audio data here
    }
}

fun stopRecording() {
    audioRecord?.stop()
    audioRecord?.release()
    audioRecord = null
}

private fun hasRequiredPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun formatElapsedTime(timeMillis: Long): String {
    val seconds = timeMillis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
