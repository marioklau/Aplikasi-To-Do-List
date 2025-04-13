import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jejakharian.Alarm
import com.example.jejakharian.Bantuan_Kalender
import com.example.jejakharian.MainActivity
import com.example.jejakharian.R
import java.util.*

class Tambah_Kalender : Fragment(R.layout.fragment_tambah_kelender) {

    private lateinit var editTextJudul: EditText
    private lateinit var editTextKeterangan: EditText
    private lateinit var editTextTanggal: EditText
    private lateinit var editTextWaktu: EditText
    private lateinit var addButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi EditText dan Button
        editTextJudul = view.findViewById(R.id.editTextJudul)
        editTextKeterangan = view.findViewById(R.id.editTextKeterangan)
        editTextTanggal = view.findViewById(R.id.editTextTanggal)
        editTextWaktu = view.findViewById(R.id.editTextWaktu)
        addButton = view.findViewById(R.id.addButton)

        // Handler untuk input Tanggal
        editTextTanggal.setOnClickListener {
            showDatePicker()
        }

        // Handler untuk input Waktu
        editTextWaktu.setOnClickListener {
            showTimePicker()
        }

        // Validasi input saat pengguna selesai mengisi
        validateInputs()

        // Handler untuk tombol addButton
        addButton.setOnClickListener {
            handleAddButtonClick()
        }
    }

    // Fungsi untuk menampilkan DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal: DD/MM/YYYY
                val formattedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                editTextTanggal.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    // Fungsi untuk menampilkan TimePickerDialog
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Format waktu: HH:MM
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editTextWaktu.setText(formattedTime)
            }, hour, minute, true) // true untuk format 24 jam

        timePickerDialog.show()
    }

    // Fungsi untuk validasi input
    private fun validateInputs() {
        // Handler untuk validasi input tanggal
        editTextTanggal.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && editTextTanggal.text.isEmpty()) {
                Toast.makeText(requireContext(), "Tanggal harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handler untuk validasi input waktu
        editTextWaktu.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && editTextWaktu.text.isEmpty()) {
                Toast.makeText(requireContext(), "Waktu harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAddButtonClick() {
        // Nonaktifkan tombol untuk mencegah klik berulang
        addButton.isEnabled = false

        val judul = editTextJudul.text.toString()
        val keterangan = editTextKeterangan.text.toString()
        val tanggal = editTextTanggal.text.toString()
        val waktu = editTextWaktu.text.toString()

        if (judul.isEmpty()) {
            Toast.makeText(requireContext(), "Judul belum diisi!", Toast.LENGTH_SHORT).show()
            addButton.isEnabled = true
            return
        }

        if (keterangan.isEmpty()) {
            Toast.makeText(requireContext(), "Keterangan belum diisi!", Toast.LENGTH_SHORT).show()
            addButton.isEnabled = true
            return
        }

        if (tanggal.isEmpty()) {
            Toast.makeText(requireContext(), "Tanggal belum diisi!", Toast.LENGTH_SHORT).show()
            addButton.isEnabled = true
            return
        }

        if (waktu.isEmpty()) {
            Toast.makeText(requireContext(), "Waktu belum diisi!", Toast.LENGTH_SHORT).show()
            addButton.isEnabled = true
            return
        }

        // Cek apakah catatan dengan judul yang sama sudah ada
        val bantuanKalender = Bantuan_Kalender(requireContext())
        if (bantuanKalender.isKalenderExists(judul)) {
            Toast.makeText(requireContext(), "Catatan dengan judul ini sudah ada!", Toast.LENGTH_SHORT).show()
            addButton.isEnabled = true
            return
        }

        // Simpan ke database
        val result = bantuanKalender.addKalender(judul, keterangan, tanggal, waktu)
        if (result != -1L) {
            Toast.makeText(requireContext(), "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show()

            // Pindah ke halaman Beranda
            (activity as MainActivity).replaceFragment(Beranda())

            // Set alarm
            setAlarm(tanggal, waktu)
        } else {
            Toast.makeText(requireContext(), "Gagal menambahkan data!", Toast.LENGTH_SHORT).show()
        }

        // Aktifkan kembali tombol setelah proses selesai
        addButton.isEnabled = true
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(tanggal: String, waktu: String) {
        try {
            val calendar = Calendar.getInstance()
            val timeParts = waktu.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            // Mengatur waktu untuk alarm
            val dateParts = tanggal.split("/")
            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1 // Bulan dimulai dari 0
            val year = dateParts[2].toInt()

            calendar.set(year, month, day, hour, minute, 0)

            // Jika waktu sudah lewat hari ini, atur untuk besok
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            Log.d("Tambah_Kalender", "Alarm set for: ${calendar.time}")

            // Mengatur alarm
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, Alarm::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.d("Tambah_Kalender", "Alarm successfully set.")
        } catch (e: Exception) {
            Log.e("Tambah_Kalender", "Error setting alarm: ${e.message}")
            Toast.makeText(requireContext(), "Error setting alarm: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}