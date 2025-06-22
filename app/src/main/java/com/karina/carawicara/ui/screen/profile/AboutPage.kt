
package com.karina.carawicara.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karina.carawicara.R
import com.karina.carawicara.data.CreditsProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    navController: NavController
) {
    val appInfo = remember { CreditsProvider.getAppInfo() }
    val credits = remember { CreditsProvider.getAppCredits() }
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About & Credits",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // App Logo
            Image(
                painter = painterResource(id = R.drawable.logo_color),
                contentDescription = "CaraWicara Logo",
                modifier = Modifier
                    .height(80.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            // Version
            Text(
                text = appInfo["Version"] ?: "1.0.0",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // App Description
            Text(
                text = "Aplikasi terapi wicara untuk anak-anak",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )

            // Copyright
            Text(
                text = "Copyright © 2025 ${appInfo["Developer"] ?: "Karina Development Team"}",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // What's New Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Version History
                Text(
                    text = "${appInfo["Version"] ?: "1.0.0"} (2025-06-04)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                FeatureSection(
                    title = "🎯 Terapi Wicara Interaktif Lengkap",
                    description = "Kontrol pembelajaran, capai kemajuan optimal!",
                    features = listOf(
                        "Flashcard dengan berbagai kategori pembelajaran",
                        "Pelafalan dengan feedback audio real-time",
                        "Kenali Aku untuk ekspresi emosi",
                        "Susun Kata untuk pembentukan kalimat",
                        "Suara Pintar untuk latihan konsonan"
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                FeatureSection(
                    title = "📊 Sistem Pelacakan Kemajuan",
                    description = "Analisis kemajuan pembelajaran yang detail!",
                    features = listOf(
                        "Riwayat terapi detail untuk setiap pasien",
                        "Grafik perkembangan mingguan dan bulanan",
                        "Export data dalam format CSV dan PDF",
                        "Analisis kemajuan berdasarkan kategori terapi"
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                FeatureSection(
                    title = "👥 Manajemen Pasien Multi-User",
                    description = "Kelola banyak pasien dengan mudah!",
                    features = listOf(
                        "Database pasien dengan informasi lengkap",
                        "Kemampuan bahasa berdasarkan usia",
                        "Pemilihan pasien untuk setiap sesi terapi",
                        "Penyimpanan data lokal yang aman"
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Credits Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Credits & Attributions",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                credits.forEach { credit ->
                    CreditCategorySection(
                        category = credit.category,
                        items = credit.items,
                        uriHandler = uriHandler
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Footer
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Terima kasih kepada semua pihak yang telah berkontribusi dalam pengembangan aplikasi CaraWicara.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LinkItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    )
}

@Composable
fun FeatureSection(
    title: String,
    description: String,
    features: List<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = description,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        features.forEach { feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "• ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun CreditCategorySection(
    category: String,
    items: List<com.karina.carawicara.data.CreditItem>,
    uriHandler: androidx.compose.ui.platform.UriHandler
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = category,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        items.forEach { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Source: ${item.source}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 1.dp)
                )

                item.license?.let { license ->
                    Text(
                        text = "License: $license",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                item.url?.let { url ->
                    Text(
                        text = url,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}