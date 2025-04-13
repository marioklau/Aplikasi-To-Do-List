package com.example.jejakharian

class Model_Kalender(
    var judul: String,
    var deskripsi: String,
    var tanggal: String,
    var waktu: String,
    var id: Int
) {
    // Getter dan Setter eksplisit
    fun getTitle(): String {
        return judul
    }

    fun setTitle(judul: String) {
        this.judul = judul
    }

    fun getDescription(): String {
        return deskripsi
    }

    fun setDescription(deskripsi: String) {
        this.deskripsi = deskripsi
    }

    fun getDate(): String {
        return tanggal
    }

    fun setDate(tanggal: String) {
        this.tanggal = tanggal
    }

    fun getTime(): String {
        return waktu
    }

    fun setTime(waktu: String) {
        this.waktu = waktu
    }

    fun getCatatanId(): Int {
        return id
    }

    fun setCatatanId(id: Int) {
        this.id = id
    }
}
