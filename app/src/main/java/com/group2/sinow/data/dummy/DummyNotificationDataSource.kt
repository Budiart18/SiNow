package com.group2.sinow.data.dummy

import com.group2.sinow.model.notification.Notification

interface DummyNotificationDataSource {
    fun getNotificationData(): List<Notification>
}

class DummyNotificationDataSourceImpl() : DummyNotificationDataSource {

    override fun getNotificationData(): List<Notification> {
        return mutableListOf(
            Notification(
                id = 1,
                type = "Notifikasi",
                title = "Tugas Baru",
                content = "Anda memiliki tugas baru untuk mata pelajaran Fisika. Silakan selesaikan sebelum batas waktu.",
                userId = 1,
                isRead = false,
                createdAt = "22/01/2023",
                updatedAt = "22/01/2023"
            ),
            Notification(
                id = 2,
                type = "Notifikasi",
                title = "Diskusi Kelas Online",
                content = "Diskusi kelas online untuk mata pelajaran Bahasa Inggris akan dimulai besok pukul 15:00 WIB.",
                userId = 2,
                isRead = false,
                createdAt = "23/01/2023",
                updatedAt = "23/01/2023"
            ),
            Notification(
                id = 3,
                type = "Notifikasi",
                title = "Pesan Dari Pengajar",
                content = "Guru Matematika memberikan tips penting untuk memahami konsep integral dalam pelajaran hari ini.",
                userId = 3,
                isRead = true,
                createdAt = "24/01/2023",
                updatedAt = "24/01/2023"
            ),
            Notification(
                id = 4,
                type = "Notifikasi",
                title = "Jadwal Ujian",
                content = "Jadwal ujian akhir semester telah diumumkan. Pastikan Anda mempersiapkan diri dengan baik.",
                userId = 4,
                isRead = false,
                createdAt = "25/01/2023",
                updatedAt = "25/01/2023"
            ),
            Notification(
                id = 5,
                type = "Notifikasi",
                title = "Prestasi Baru",
                content = "Selamat! Anda berhasil menyelesaikan modul pelajaran Kimia dengan nilai tinggi.",
                userId = 5,
                isRead = true,
                createdAt = "26/01/2023",
                updatedAt = "26/01/2023"
            ),
            Notification(
                id = 6,
                type = "Notifikasi",
                title = "Pembayaran Biaya Pendaftaran",
                content = "Jangan lupa melakukan pembayaran biaya pendaftaran untuk semester depan sebelum tanggal 5 Februari 2023.",
                userId = 6,
                isRead = false,
                createdAt = "27/01/2023",
                updatedAt = "27/01/2023"
            ),
            Notification(
                id = 7,
                type = "Notifikasi",
                title = "Webinar Pemrograman",
                content = "Hari ini ada webinar khusus tentang pengembangan aplikasi mobile. Ikuti dan perluas pengetahuan Anda!",
                userId = 7,
                isRead = true,
                createdAt = "28/01/2023",
                updatedAt = "28/01/2023"
            ),
            Notification(
                id = 8,
                type = "Notifikasi",
                title = "Pengumuman Kompetisi",
                content = "Pemenang kompetisi penulisan esai akan diumumkan pada Senin depan. Semangat untuk semua peserta!",
                userId = 8,
                isRead = false,
                createdAt = "29/01/2023",
                updatedAt = "29/01/2023"
            ),
            Notification(
                id = 9,
                type = "Notifikasi",
                title = "Maintenance Platform",
                content = "Aplikasi sedang dalam perbaikan. Mohon maaf atas ketidaknyamanannya. Silakan cek lagi dalam beberapa jam.",
                userId = 9,
                isRead = true,
                createdAt = "30/01/2023",
                updatedAt = "30/01/2023"
            ),
            Notification(
                id = 10,
                type = "Notifikasi",
                title = "Pemberitahuan Jadwal",
                content = "Perubahan jadwal pelajaran untuk minggu depan telah diterapkan. Pastikan untuk memeriksa jadwal Anda.",
                userId = 10,
                isRead = false,
                createdAt = "31/01/2023",
                updatedAt = "31/01/2023"
            )
        )
    }
}
