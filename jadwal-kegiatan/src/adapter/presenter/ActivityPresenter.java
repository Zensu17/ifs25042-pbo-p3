package adapter.presenter;

import domain.entity.Activity;
import java.util.List;

/** Memformat data kegiatan menjadi output layar. */
public class ActivityPresenter {
    /** Memformat satu data menjadi baris teks untuk ditampilkan. */
    private String format(Activity a) {
        return String.format("[%d] %s | Hari: %s | Pukul: %s", a.getId(), a.getTitle(), a.getDay().label(), a.getTime());
    }

    /** Helper umum untuk menampilkan daftar; menampilkan pesan kosong jika tidak ada data. */
    private void printList(List<Activity> list, String header, String emptyMessage) {
        System.out.println(header);
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        list.forEach(a -> System.out.println(format(a)));
    }

    /** Menampilkan daftar semua kegiatan. */
    public void showActivities(List<Activity> list) {
        printList(list, "Daftar Kegiatan:", "- Data kegiatan belum tersedia!");
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Activity> list, String keyword) {
        printList(list, "Hasil Pencarian: \"" + keyword + "\"", "- Kegiatan tidak ditemukan!");
    }

    /** Menampilkan daftar yang sudah diurutkan. */
    public void showSortedActivities(List<Activity> list) {
        printList(list, "Daftar Kegiatan (Terurut):", "- Data kegiatan belum tersedia!");
    }

    /** Menampilkan pesan sukses setelah menambah kegiatan. */
    public void showAddSuccess(Activity a) {
        System.out.println("Berhasil menambah kegiatan: " + format(a));
    }

    /** Menampilkan pesan sukses setelah mengubah kegiatan. */
    public void showUpdateSuccess(Activity a) {
        System.out.println("Berhasil mengubah kegiatan.");
    }

    /** Menampilkan pesan gagal saat mengubah kegiatan. */
    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah kegiatan dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan sukses setelah menghapus kegiatan. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus kegiatan.");
    }

    /** Menampilkan pesan gagal saat menghapus kegiatan. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus kegiatan dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan saat pilihan menu tidak dikenali. */
    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    /** Menampilkan pesan saat ID yang dimasukkan tidak valid. */
    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }

    /** Menampilkan pesan saat opsi pengurutan tidak valid. */
    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }

    /** Menampilkan pesan kegagalan validasi yang dilempar use case. */
    public void showError(String message) {
        System.out.println("[!] " + message);
    }
}
