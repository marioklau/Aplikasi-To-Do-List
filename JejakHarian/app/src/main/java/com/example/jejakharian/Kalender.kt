import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.jejakharian.R
import java.util.Calendar

class Kalender : Fragment(R.layout.fragment_kalender) {

    private lateinit var tanggalKalenderEditText: EditText
    private lateinit var pilihTanggalButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tanggalKalenderEditText = view.findViewById(R.id.tanggal_kalender)
        pilihTanggalButton = view.findViewById(R.id.pilih_tanggal)

        pilihTanggalButton.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    tanggalKalenderEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}