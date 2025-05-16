package com.karina.carawicara.data.repository

import android.util.Log
import com.karina.carawicara.data.CaraWicaraDatabase
import com.karina.carawicara.data.LanguageAbility
import com.karina.carawicara.data.Patient
import com.karina.carawicara.data.TherapyHistory
import com.karina.carawicara.data.entity.PatientEntity
import com.karina.carawicara.data.entity.TherapyHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate

class PatientRepository (private val database: CaraWicaraDatabase){
    val allPatients: Flow<List<Patient>> = database.patientDao().getAllPatients().map { entities ->
        entities.map { it.toPatient() }
    }

    suspend fun insertPatient(patient: Patient) {
        withContext(Dispatchers.IO) {
            val patientEntity = PatientEntity(
                id = patient.id,
                name = patient.name,
                birthDate = patient.birthDate,
                address = patient.address,
                languageAbilitiesJson = patient.languageAbilitiesToJson()
            )
            database.patientDao().insertPatient(patientEntity)
        }
    }

    suspend fun updatePatient(patient: Patient) {
        withContext(Dispatchers.IO) {
            val patientEntity = PatientEntity(
                id = patient.id,
                name = patient.name,
                birthDate = patient.birthDate,
                address = patient.address,
                languageAbilitiesJson = patient.languageAbilitiesToJson()
            )
            database.patientDao().updatePatient(patientEntity)
        }
    }

    suspend fun deletePatient(patientId: String) {
        withContext(Dispatchers.IO) {
            database.patientDao().deletePatientById(patientId)
        }
    }

    suspend fun getPatientById(patientId: String): Patient? {
        return withContext(Dispatchers.IO) {
            database.patientDao().getPatientById(patientId)?.toPatient()
        }
    }

    fun generateLanguageAbilitiesByAge(ageYears: Int, ageMonths: Int): List<LanguageAbility> {
        val totalMonths = ageYears * 12 + ageMonths

        return when {
            totalMonths <= 6 -> generateLanguageAbilitiesFor0To6Months()
            totalMonths <= 12 -> generateLanguageAbilitiesFor7To12Months()
            totalMonths <= 18 -> generateLanguageAbilitiesFor13To18Months()
            totalMonths <= 24 -> generateLanguageAbilitiesFor19To24Months()
            totalMonths <= 36 -> generateLanguageAbilitiesFor2To3Years()
            totalMonths <= 48 -> generateLanguageAbilitiesFor3To4Years()
            totalMonths <= 60 -> generateLanguageAbilitiesFor4To5Years()
            totalMonths <= 72 -> generateLanguageAbilitiesFor5To6Years()
            totalMonths <= 84 -> generateLanguageAbilitiesFor6To7Years()
            else -> generateLanguageAbilitiesForOlderChildren()
        }
    }

    private fun generateLanguageAbilitiesFor0To6Months(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Mengulangi suara yang sama"),
            LanguageAbility("2", "Sering kali membuat suara 'koo' dan 'gurgles', dan suara-suara yang menyenangkan "),
            LanguageAbility("3", "Menggunakan tangisan yang berbeda-beda untuk mengutarakan kebutuhan yang berbeda-beda"),
            LanguageAbility("4", "Tersenyum bila diajak berbicara"),
            LanguageAbility("5", "Mengenali suara manusia"),
            LanguageAbility("6", "Melokalisasi suara dengan cara menoleh"),
            LanguageAbility("7", "Mendengarkan pembicaraan"),
            LanguageAbility("8", "Menggunakan konsonan /p/, /b/, dan /m/ ketika mengoceh"),
            LanguageAbility("9", "Menggunakan suara atau isyarat (gesture) untuk member tahu keinginan"),
        )
    }

    private fun generateLanguageAbilitiesFor7To12Months(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Mengerti arti tidak dan panas"),
            LanguageAbility("2", "Dapat memberi respon untuk permintaan yang sederhana "),
            LanguageAbility("3", "Mengerti dan memberi respon pada namanya sendiri"),
            LanguageAbility("4", "Mendengarkan dan meniru beberapa suara"),
            LanguageAbility("5", "Mengenali kata untuk benda sehari-hari (misalnya susu, sepatu, cangkir, dll)"),
            LanguageAbility("6", "'mengoceh' dengan menggunakan suara yang panjang dan pendek"),
            LanguageAbility("7", "Menggunakan intonasi seperti lagu ketika 'mengoceh'"),
            LanguageAbility("8", "Menggunakan bermacam-macam suara ketika mengoceh"),
            LanguageAbility("9", "Menirukan beberapa suara bicara orang dewasa dan intonasinya"),
            LanguageAbility("10", "Menggunakan suara bicara selain tangisan untuk mendapatkan perhatian"),
            LanguageAbility("11", "Mendengarkan bila diajak berbicara"),
            LanguageAbility("12", "Menggunakan suara yang mendekati suara yang didengar"),
            LanguageAbility("13", "Mulai merubah 'ocehan' ke 'bahasa bulan'"),
            LanguageAbility("14", "Mulai menggunakan bicara dengan tujuan"),
            LanguageAbility("15", "Hanya menggunakan kata benda"),
            LanguageAbility("16", "Memiliki pengucapan (ekspresif) kosakata 1-3 kata"),
            LanguageAbility("17", "Mengerti perintah sederhana"),
        )
    }

    private fun generateLanguageAbilitiesFor13To18Months(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Menggunakan intonasi yang mengikuti pola bicara orang dewasa"),
            LanguageAbility("2", "Menggunakan 'echolalia' dan 'bahasa bulan' (jargon)"),
            LanguageAbility("3", "Tidak mengucapkan beberapa konsonan depan dan hampir seluruh konsonan akhir"),
            LanguageAbility("4", "Bicara hampir keseluruhannya tidak dapat dimengerti"),
            LanguageAbility("5", "Mengikuti perintah sederhana"),
            LanguageAbility("6", "Mengenali 1-3 bagian tubuh"),
            LanguageAbility("7", "Memiliki pengucapan (ekspresif) kosakata 3-10 kata atau lebih (kebanyakan kata benda)"),
            LanguageAbility("8", "Memadukan vokalisasi dan isyarat"),
            LanguageAbility("9", "Membuat permintaan untuk hal-hal yang lebih diinginkan"),
        )
    }

    private fun generateLanguageAbilitiesFor19To24Months(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Lebih sering menggunakan kata daripada 'bahasa bulan' (jargon)"),
            LanguageAbility("2", "Memiliki pengucapan (ekspresif) kosakata 50-100 kata atau lebih"),
            LanguageAbility("3", "Memiliki pemahaman (reseptif) kosakata 300 kata atau lebih"),
            LanguageAbility("4", "Mulai memadu kata benda dan kata kerja"),
            LanguageAbility("5", "Mulai menggunakan kata ganti orang"),
            LanguageAbility("6", "Kendali suara masih tidak stabil"),
            LanguageAbility("7", "Menggunakan intonasi yang benar ketika bertanya"),
            LanguageAbility("8", "Bicara 25-50% dapat dimengerti orang lain"),
            LanguageAbility("9", "Menjawab pertanyaan 'ini apa?'"),
            LanguageAbility("10", "Senang mendengarkan cerita"),
            LanguageAbility("11", "Mengenali 5 bagian tubuh"),
            LanguageAbility("12", "Secara benar dapat menamakan beberapa benda sehari-hari"),
        )
    }

    private fun generateLanguageAbilitiesFor2To3Years(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Bicara 50-75% dapat dimengerti orang lain"),
            LanguageAbility("2", "Mengerti satu dan semua"),
            LanguageAbility("3", "Mengucapkan keinginan untuk ke kamar mandi (sebelum, sedang, atau setelah kejadian"),
            LanguageAbility("4", "Meminta benda dengan menamakannya"),
            LanguageAbility("5", "Menunjuk kepada gambar didalam buku bila diminta"),
            LanguageAbility("6", "Mengenali beberapa bagian tubuh"),
            LanguageAbility("7", "Mengikuti perintah sederhana dan menjawab pertanyaan sederhana"),
            LanguageAbility("8", "Senang mendengarkan cerita pendek, lagu dan sajak"),
            LanguageAbility("9", "Menanyakan 1-2 kata pertanyaan"),
            LanguageAbility("10", "Menggunakan 3-4 kata frase"),
            LanguageAbility("11", "Menggunakan preposisi"),
            LanguageAbility("12", "Menggunakan 'echolalia' bila kesulitan berbicara"),
            LanguageAbility("13", "Memiliki pengucapan (ekspresif) kosakata 50-200 kata dan berkembang dengan pesat pada tahap ini"),
            LanguageAbility("14", "Memiliki pemahaman reseptif kosakata 500 kata atau lebih"),
            LanguageAbility("15", "Memperlihatkan kesalahan dalam pemakaian tata bahasa"),
            LanguageAbility("16", "Mengerti hampir keseluruhannya yang dikatakan kepadanya"),
            LanguageAbility("17", "Sering mengulang, terutama kata permulaan 'saya'/nama dan suku kata pertama"),
            LanguageAbility("18", "Berbicara dengan suara keras"),
            LanguageAbility("19", "Nada suara mulai meninggi"),
            LanguageAbility("20", "Menggunakan huruf hidup dengan baik"),
            LanguageAbility("21", "Secara konsisten menggunakan konsonan awal (walaupun beberapa masih tidak dapat diucapkan dengan baik"),
            LanguageAbility("22", "Sering menghilangkan konsonan tengah"),
            LanguageAbility("23", "Sering menghilangkan atau mengganti konsonan akhir"),
        )
    }

    private fun generateLanguageAbilitiesFor3To4Years(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Mengerti fungsi dengan benda"),
            LanguageAbility("2", "Mengerti perbedaan dari arti kata (besar-kecil, diatas-didalam, berhenti-jalan)"),
            LanguageAbility("3", "Mengikuti perintah 2-3 bagian"),
            LanguageAbility("4", "Bertanya dan menjawab pertanyaan sederhana (siapa, apa, dimana, kenapa)"),
            LanguageAbility("5", "Sering bertanya dan meminta jawaban yang terperinci"),
            LanguageAbility("6", "Menggunakan analogi yang sederhana"),
            LanguageAbility("7", "Menggunakan bahsa untuk mengekspresikan perasaan"),
            LanguageAbility("8", "Menggunakan 4-5 kata dalam kalimat"),
            LanguageAbility("9", "Mengulang kalimat 6-13 suku kata secara benar"),
            LanguageAbility("10", "Mengnali benda dengan nama"),
            LanguageAbility("11", "Memanipulasi orang dewasa dan teman sebaya"),
            LanguageAbility("12", "Kadang-kadang 'echolalia' masih digunakan"),
            LanguageAbility("13", "Lebih sering menggunakan kata benda dan kata kerja"),
            LanguageAbility("14", "Sadar akan waktu yang telah lalu dan yang akan datang"),
            LanguageAbility("15", "Memiliki pengucapan (ekspresif) kosakata 800-1500 kata atau lebih"),
            LanguageAbility("16", "Memiliki pemahaman (reseptif) kosakata 1500-2000 kata atau lebih"),
            LanguageAbility("17", "Kadang kala mengulang nama, terbata-bata, kesulitan mengatur napas, dan meringis"),
            LanguageAbility("18", "Berbisik"),
            LanguageAbility("19", "Bicara 80% dapat dipahami"),
            LanguageAbility("20", "Walaupun masih banyak kesalahan, tata bahasa sudah membaik"),
            LanguageAbility("21", "Dapat menceritakan dua kejadian secara urut"),
        )
    }

    private fun generateLanguageAbilitiesFor4To5Years(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Mengerti konsep jumlah sampai dengan 3"),
            LanguageAbility("2", "Mengerti spatial konsep"),
            LanguageAbility("3", "Mengenali 1-3 warna"),
            LanguageAbility("4", "Memiliki pemahaman (reseptif) kosakata 2800 kata atau lebih"),
            LanguageAbility("5", "Menghitung sampai 10 secara role"),
            LanguageAbility("6", "Mendengarkan cerita pendek"),
            LanguageAbility("7", "Menjawab pertanyaan tentang fungsi"),
            LanguageAbility("8", "Menggunakan tata bahsa dalam kalimat yang benar"),
            LanguageAbility("9", "Memiliki pengucapan (ekspresif) kosakata 900-2000 kata atau lebih"),
            LanguageAbility("10", "Menggunakan kalimat dengan 4-8 kata"),
            LanguageAbility("11", "Menjawab pertanyaan 2 bagian"),
            LanguageAbility("12", "Menanyakan arti dari kata"),
            LanguageAbility("13", "Senang akan sajak, ritme dan suku kata tak berarti"),
            LanguageAbility("14", "Menggunakan konsonan dengan 90% ketepatan"),
            LanguageAbility("15", "Bicara biasanya dapat dimengerti oleh orang lain"),
            LanguageAbility("16", "Dapat bercerita tantang pengalaman di sekolah, di rumah teman, dll"),
            LanguageAbility("17", "Dapat menceritakan kembali cerita panjang"),
            LanguageAbility("18", "Memperhatikan bila diceritakan dan menjawab pertanyaan sederhana tentang cerita tersebut"),
        )
    }

    private fun generateLanguageAbilitiesFor5To6Years(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Menamakan 6 warna dasar dan 3 bentuk dasar"),
            LanguageAbility("2", "Mengikuti perintah yang diberikan dalam kelompok"),
            LanguageAbility("3", "Mengikuti perintah 3 bagian"),
            LanguageAbility("4", "Menanyakan pertanyaan 'bagaimana?'"),
            LanguageAbility("5", "Menjawab secara verbal pertanyaan 'hai' dan 'apa kabar'"),
            LanguageAbility("6", "Menggunakan kata untuk sesuatu yang telah berlalu dan akan datang"),
            LanguageAbility("7", "Menggunakan kata penghubung"),
            LanguageAbility("8", "Memiliki pengucapan (ekspresif) kosakata kurang lebih 13.000 kata"),
            LanguageAbility("9", "Menamakan lawan kata"),
            LanguageAbility("10", "Secara urut menamakan nama hari"),
            LanguageAbility("11", "Menghitung sampai 30 secara mengurutkan (rote)"),
            LanguageAbility("12", "Kosakata secara drastis meningkat"),
            LanguageAbility("13", "Panjang kata dalam kalimat menurun hingga 4-6 kata dalam kalimat"),
        )
    }

    private fun generateLanguageAbilitiesFor6To7Years(): List<LanguageAbility> {
        return listOf(
            LanguageAbility("1", "Menamakan beberapa huruf, angka, dan mata uang"),
            LanguageAbility("2", "Mengurutkan angka dan dapat mengucapkan abjad"),
            LanguageAbility("3", "Mengerti kanan dan kiri"),
            LanguageAbility("4", "Menggunakan makin banyak lagi kata-kata yang lebih kompleks untuk menjelaskan sesuatu dan mampu mengadakan percakapan"),
            LanguageAbility("5", "Memiliki pemahaman kosakata kurang lebih 20.000 kata"),
            LanguageAbility("6", "Menggunakan panjang kalimat sampai dengan 6 kata"),
            LanguageAbility("7", "Mengerti hampir keseluruhan konsep tentang waktu"),
            LanguageAbility("8", "Dapat menghitung sampai dengan 100 secara role"),
            LanguageAbility("9", "Menggunakan hampir seluruh aturan untuk perubahan kata dengan benar"),
            LanguageAbility("10", "Menggunakan kalimat pasif dengan benar"),
        )
    }

    private fun generateLanguageAbilitiesForOlderChildren(): List<LanguageAbility> {
        return emptyList()
    }

    fun getAllTherapyHistories(): Flow<List<TherapyHistory>> {
        return database.therapyHistoryDao().getAllTherapyHistories().map { entities ->
            entities.map { it.toTherapyHistory() }
        }
    }

    fun getTherapyHistoriesForPatient(patientId: String): Flow<List<TherapyHistory>> {
        return database.therapyHistoryDao().getTherapyHistoriesForPatient(patientId).map { entities ->
            entities.map { it.toTherapyHistory() }
        }
    }

    suspend fun getTherapyHistoryById(historyId: String): TherapyHistory? {
        return withContext(Dispatchers.IO) {
            val entity = database.therapyHistoryDao().getTherapyHistoryById(historyId)
            entity?.toTherapyHistory()
        }
    }

    suspend fun insertTherapyHistory(therapyHistory: TherapyHistory) {
        withContext(Dispatchers.IO) {
            val therapyHistoryEntity = TherapyHistoryEntity(
                id = therapyHistory.id,
                patientId = therapyHistory.patientId,
                therapyType = therapyHistory.therapyType,
                date = therapyHistory.date,
                score = therapyHistory.score,
                totalQuestions = therapyHistory.totalQuestions,
                progressPercentage = therapyHistory.progressPercentage,
                notes = therapyHistory.notes,
                categoryId = therapyHistory.categoryId
            )
            database.therapyHistoryDao().insertTherapyHistory(therapyHistoryEntity)
        }
    }

    suspend fun deleteTherapyHistory(historyId: String) {
        withContext(Dispatchers.IO) {
            database.therapyHistoryDao().deleteTherapyHistory(historyId)
        }
    }

    suspend fun deleteTherapyHistoriesForPatient(patientId: String) {
        withContext(Dispatchers.IO) {
            database.therapyHistoryDao().deleteTherapyHistoriesForPatient(patientId)
        }
    }
}

private fun PatientEntity.toPatient(): Patient {
    val languageAbilities = try {
        languageAbilitiesFromJson(this.languageAbilitiesJson)
    } catch (e: Exception) {
        Log.e("PatientRepository", "Error parsing language abilities for patient ${this.id}", e)
        emptyList()
    }

    return Patient(
        id = this.id,
        name = this.name,
        birthDate = this.birthDate,
        age = calculateAge(this.birthDate),
        address = this.address,
        languageAbilities = languageAbilities
    )
}

private fun TherapyHistoryEntity.toTherapyHistory(): TherapyHistory {
    return TherapyHistory(
        id = this.id,
        patientId = this.patientId,
        therapyType = this.therapyType,
        date = this.date,
        score = this.score,
        totalQuestions = this.totalQuestions,
        progressPercentage = this.progressPercentage,
        notes = this.notes,
        categoryId = this.categoryId,
        showLine = true
    )
}

private fun calculateAge(birthDate: LocalDate): Int {
    return java.time.Period.between(birthDate, LocalDate.now()).years
}

private fun Patient.languageAbilitiesToJson(): String {
    val jsonArray = StringBuilder("[")
    languageAbilities.forEachIndexed { index, ability ->
        jsonArray.append("""{"id":"${ability.id}","description":"${ability.description}","isSelected":${ability.isSelected}}""")
        if (index < languageAbilities.size - 1) {
            jsonArray.append(",")
        }
    }
    jsonArray.append("]")
    return jsonArray.toString()
}

private fun languageAbilitiesFromJson(json: String): List<LanguageAbility> {
    if (json.isEmpty() || json == "null") {
        return emptyList()
    }

    try {
        val abilities = mutableListOf<LanguageAbility>()
        val jsonArray = json.trim()
        if (jsonArray.startsWith("[") && jsonArray.endsWith("]")) {
            val items = jsonArray.substring(1, jsonArray.length - 1).split("},")

            items.forEach { item ->
                val cleanItem = if (item.endsWith("}")) item else "$item}"

                val idMatch = Regex(""""id":"([^"]+)"""").find(cleanItem)
                val descMatch = Regex(""""description":"([^"]+)"""").find(cleanItem)
                val selectedMatch = Regex(""""isSelected":(true|false)""").find(cleanItem)

                if (idMatch != null && descMatch != null && selectedMatch != null) {
                    val id = idMatch.groupValues[1]
                    val description = descMatch.groupValues[1]
                    val isSelected = selectedMatch.groupValues[1].toBoolean()

                    abilities.add(LanguageAbility(
                        id = id,
                        description = description,
                        isSelected = isSelected
                    ))
                }
            }
        }
        return abilities
    } catch (e: Exception) {
        Log.e("PatientRepository", "Error parsing language abilities JSON: $json", e)
        return emptyList()
    }
}