package com.karina.carawicara.ui.screen.kenaliAku

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.karina.carawicara.R
import com.karina.carawicara.ui.component.CameraPreview
import com.karina.carawicara.ui.component.StageBox
import java.io.File
import java.util.Random

private const val REQUEST_CODE_PERMISSIONS = 1001

@Composable
fun KenaliAkuRecordPage(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val filesDir = context.filesDir

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    val CAMERAX_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
    )

    val allPermissionsGranted = hasRequiredPermissions(context, CAMERAX_PERMISSIONS)

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(31.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "Cancel",
                    tint = Color(0xFF4A73B9), // Warna biru
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navHostController.popBackStack()
                        }
                        .padding(4.dp)
                )
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                    StageBox(stage = 0, onClick = { /* Handle click here */ })
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                if (allPermissionsGranted) {
                    CameraPreview(
                        controller = controller,
                        modifier = Modifier
                            .aspectRatio(3f / 5)
                            .padding(32.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (allPermissionsGranted) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_flip),
                            contentDescription = "Putar Balik",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    flipCamera(controller)
                                }
                                .padding(4.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_record),
                            contentDescription = "Rekam",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    takePicture(controller, context, filesDir, navHostController)
                                }
                                .padding(4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    if (!allPermissionsGranted) {
        ActivityCompat.requestPermissions(
            context as Activity,
            CAMERAX_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }
}

private fun hasRequiredPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun flipCamera(controller: LifecycleCameraController) {
    isUsingFrontCamera = !isUsingFrontCamera
    val lensFacing = if (isUsingFrontCamera) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
    controller.cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
}

@SuppressLint("MissingPermission")
private fun takePicture(
    controller: LifecycleCameraController,
    context: Context,
    filesDir: File,
    navHostController: NavHostController
) {
    val outputFile = File(filesDir, "my-picture.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Handle the success of images capture
                Toast.makeText(context, "Picture taken successfully!", Toast.LENGTH_SHORT).show()

                // Generate a random accuracy value between 50 and 70
                val random = Random()
                val accuracy = 50 + random.nextInt(21)

                // Save the images path to SavedStateHandle
                navHostController.currentBackStackEntry?.savedStateHandle?.set("imagePath", outputFile.absolutePath)

                // Navigate to the result page with the accuracy
                val route = "kenaliAkuResultPage/$accuracy"
                navHostController.navigate(route)
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle any errors during images capture
                Toast.makeText(context, "Failed to take picture: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
