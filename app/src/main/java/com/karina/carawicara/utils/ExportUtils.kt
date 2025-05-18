package com.karina.carawicara.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.karina.carawicara.data.TherapyHistory
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter

class ExportUtils {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        fun exportTherapyHistoriesToCsv(
            context: Context,
            therapyHistories: List<TherapyHistory>,
            patientName: String
        ): Uri? {
            try {
                val fileName = "Riwayat_Terapi_${patientName.replace("", "_")}.csv"
                val file = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(file)

                val header = "Tanggal,Jenis Terapi,Skor,Total Pertanyaan,Persentase Kemajuan,Catatan\n"
                outputStream.write(header.toByteArray())

                therapyHistories.forEach { history ->
                    val line = "${history.date.format(dateFormatter)}," +
                            "${history.therapyType}," +
                            "${history.score}," +
                            "${history.totalQuestions}," +
                            "${history.progressPercentage}," +
                            "\"${history.notes.replace("\"", "\"\"")}\"\n"
                    outputStream.write(line.toByteArray())
                }

                outputStream.close()

                return FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun shareFile(context: Context, fileUri: Uri, fileType: String, fileName: String) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = fileType
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_SUBJECT, "Riwayat Terapi")
                putExtra(Intent.EXTRA_TEXT, "Terlampir riwayat terapi pasien: $fileName")
            }
            context.startActivity(Intent.createChooser(intent, "Bagikan melalui"))
        }
    }
}