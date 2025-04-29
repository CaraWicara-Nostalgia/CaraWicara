package com.karina.carawicara.ui.screen.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karina.carawicara.data.LanguageAbility
import com.karina.carawicara.data.Patient
import com.karina.carawicara.data.TherapyHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.Period
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class PatientViewModel : ViewModel() {
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    private val _newPatientName = MutableStateFlow("")
    val newPatientName: StateFlow<String> = _newPatientName

    private val _newPatientBirthDate = MutableStateFlow<LocalDate?>(null)
    val newPatientBirthDate: StateFlow<LocalDate?> = _newPatientBirthDate

    private val _newPatientAddress = MutableStateFlow("")
    val newPatientAddress: StateFlow<String> = _newPatientAddress

    private val _newPatientAge = MutableStateFlow(0)
    val newPatientAge: StateFlow<Int> = _newPatientAge

    private val _newPatientAgeMonths = MutableStateFlow(0)
    val newPatientAgeMonths: StateFlow<Int> = _newPatientAgeMonths

    private val _languageAbilities = MutableStateFlow<List<LanguageAbility>>(emptyList())
    val languageAbilities: StateFlow<List<LanguageAbility>> = _languageAbilities

    private val _therapyHistories = MutableStateFlow<Map<String, List<TherapyHistory>>>(emptyMap())
    val therapyHistories: StateFlow<Map<String, List<TherapyHistory>>> = _therapyHistories

    init {
        localDummyPatients()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun localDummyPatients() {
        val patientId = UUID.randomUUID().toString()

        val dummyPatients = listOf(
            Patient(
                id = patientId,
                name = "Cody Fisher",
                birthDate = LocalDate.of(2018, 1, 1),
                age = 6,
                address = "Jl. Bunga Melati No. 08, Kota Malang",
                languageAbilities = listOf(
                    LanguageAbility("1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("2", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("3", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("4", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("5", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("6", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."),
                    LanguageAbility("7", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                )
            )
        )
        _patients.value = dummyPatients
    }

    private fun getLanguageAbilitiesByAge(years: Int, months: Int): List<LanguageAbility> {
        val totalMonths = years * 12 + months

        return when {
            totalMonths <= 6 -> getLanguageAbilitiesFor0To6Months()
            totalMonths <= 12 -> getLanguageAbilitiesFor7To12Months()
            totalMonths <= 18 -> getLanguageAbilitiesFor13To18Months()
            totalMonths <= 24 -> getLanguageAbilitiesFor19To24Months()
            totalMonths <= 36 -> getLanguageAbilitiesFor2To3Years()
            totalMonths <= 48 -> getLanguageAbilitiesFor3To4Years()
            totalMonths <= 60 -> getLanguageAbilitiesFor4To5Years()
            totalMonths <= 72 -> getLanguageAbilitiesFor5To6Years()
            totalMonths <= 84 -> getLanguageAbilitiesFor6To7Years()
            else -> getLanguageAbilitiesForOlderChildren()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLanguageAbilitiesBasedOnAge(birthDate: LocalDate) {
        val (years, months) = calculateAgeInYearsAndMonths(birthDate)
        _newPatientAge.value = years
        _newPatientAgeMonths.value = months
        _languageAbilities.value = getLanguageAbilitiesByAge(years, months)
    }

    private fun updateLanguageAbilitiesBasedOnAge() {
        val birthDate = _newPatientBirthDate.value ?: return
        updateLanguageAbilitiesBasedOnAge(birthDate)
    }

    private fun getLanguageAbilitiesFor0To6Months(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor7To12Months(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor13To18Months(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor19To24Months(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor2To3Years(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor3To4Years(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor4To5Years(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor5To6Years(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesFor6To7Years(): List<LanguageAbility> {
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

    private fun getLanguageAbilitiesForOlderChildren(): List<LanguageAbility> {
        return emptyList()
    }

    // Update nama pasien baru
    fun updateNewPatientName(name: String) {
        _newPatientName.value = name
    }

    // Update tanggal lahir pasien baru dan update kemampuan bahasa berdasarkan umur
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateNewPatientBirthDate(date: LocalDate) {
        _newPatientBirthDate.value = date
        updateLanguageAbilitiesBasedOnAge()
    }

    // Update alamat pasien baru
    fun updateNewPatientAddress(address: String) {
        _newPatientAddress.value = address
    }

    // Toggle selection status for a language ability
    fun toggleLanguageAbility(id: String) {
        val updatedList = _languageAbilities.value.map { ability ->
            if (ability.id == id) {
                ability.copy(isSelected = !ability.isSelected)
            } else {
                ability
            }
        }
        _languageAbilities.value = updatedList
    }

    // Calculate age from birth date in years
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }

    // Calculate age in years and months
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateAgeInYearsAndMonths(birthDate: LocalDate): Pair<Int, Int> {
        val period = Period.between(birthDate, LocalDate.now())
        return Pair(period.years, period.months)
    }

    // Add new patient to list
    fun addNewPatient() {
        val name = _newPatientName.value
        val birthDate = _newPatientBirthDate.value ?: return
        val address = _newPatientAddress.value
        val age = _newPatientAge.value

        // Get selected language abilities
        val selectedAbilities = _languageAbilities.value.filter { it.isSelected }

        val newPatient = Patient(
            id = UUID.randomUUID().toString(),
            name = name,
            birthDate = birthDate,
            age = age,
            address = address,
            languageAbilities = selectedAbilities
        )

        // Add new patient to list
        _patients.value += newPatient

        // Reset form
        resetNewPatientForm()
    }

    // Reset new patient form
    private fun resetNewPatientForm() {
        _newPatientName.value = ""
        _newPatientBirthDate.value = null
        _newPatientAddress.value = ""
        _newPatientAge.value = 0
        _newPatientAgeMonths.value = 0
        _languageAbilities.value = emptyList()
    }

    // Get age description for display
    fun getAgeDescription(years: Int, months: Int): String {
        return when {
            years == 0 && months < 12 -> "$months bulan"
            years > 0 && months == 0 -> "$years tahun"
            else -> "$years tahun $months bulan"
        }
    }

    // Fungsi untuk mendapatkan therapy history berdasarkan patient id
    fun getTherapyHistoriesForPatient(patientId: String): List<TherapyHistory> {
        return _therapyHistories.value[patientId] ?: emptyList()
    }

    // Fungsi untuk menambahkan therapy history baru
    fun addTherapyHistory(patientId: String, therapyHistory: TherapyHistory) {
        val currentHistories = _therapyHistories.value.toMutableMap()
        val patientHistories = currentHistories[patientId]?.toMutableList() ?: mutableListOf()
        patientHistories.add(therapyHistory)
        currentHistories[patientId] = patientHistories
        _therapyHistories.value = currentHistories
    }
}

class PatientViewModelFactory : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            return PatientViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}