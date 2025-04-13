import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jejakharian.R
import com.example.jejakharian.AdopsiKalender
import com.example.jejakharian.Bantuan_Kalender
import com.example.jejakharian.Model_Kalender

public class Beranda : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bantuanKalender: Bantuan_Kalender
    private val kalenderList = ArrayList<Model_Kalender>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout fragment dengan kode
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

        // Menginisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi database helper
        bantuanKalender = Bantuan_Kalender(requireContext())

        // Memuat data dari database
        loadKalenderData()

        // Menyambungkan adapter ke RecyclerView
        val adapter = AdopsiKalender(requireContext(), kalenderList)
        recyclerView.adapter = adapter

        // Menambahkan ItemTouchHelper untuk menghapus catatan
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val kalender = kalenderList[position]

                // Hapus dari database
                bantuanKalender.deleteKalender(kalender.id)

                // Hapus dari daftar
                kalenderList.removeAt(position)
                recyclerView.adapter?.notifyItemRemoved(position)
            }
        }

        // Menghubungkan ItemTouchHelper ke RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    fun loadKalenderData() {
        val cursor: Cursor = bantuanKalender.showData()
        kalenderList.clear()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val judul = cursor.getString(1)
            val deskripsi = cursor.getString(2)
            val tanggal = cursor.getString(3)
            val waktu = cursor.getString(4)
            kalenderList.add(Model_Kalender(judul, deskripsi, tanggal, waktu, id))
        }
        cursor.close() // Jangan lupa menutup cursor
        recyclerView.adapter?.notifyDataSetChanged() // Memberitahu adapter untuk memperbarui tampilan
    }
}
