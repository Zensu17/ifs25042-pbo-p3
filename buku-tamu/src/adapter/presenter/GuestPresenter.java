package adapter.presenter;

import domain.entity.Guest;
import java.util.List;

/** Memformat data tamu menjadi output layar. */
public class GuestPresenter {
    /** Memformat satu data menjadi baris teks untuk ditampilkan. */
    private String format(Guest g) {
        return String.format("[%d] %s | Tujuan: %s", g.getId(), g.getName(), g.getPurpose());
    }

    /** Helper umum untuk menampilkan daftar; menampilkan pesan kosong jika tidak ada data. */
    private void printList(List<Guest> guests, String header) {
        System.out.println(header);
        if (guests.isEmpty()) {
            System.out.println("- Data tamu belum tersedia!");
            return;
        }
        guests.forEach(g -> System.out.println(format(g)));
    }

    /** Menampilkan daftar semua tamu. */
    public void showGuests(List<Guest> guests) {
        printList(guests, "Daftar Tamu:");
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Guest> guests, String keyword) {
        System.out.println("Hasil Pencarian: \"" + keyword + "\"");
        if (guests.isEmpty()) {
            System.out.println("- Tamu tidak ditemukan!");
            return;
        }
        guests.forEach(g -> System.out.println(format(g)));
    }

    /** Menampilkan pesan sukses setelah menambah tamu. */
    public void showRegisterSuccess(Guest g) {
        System.out.println("Berhasil mendaftarkan tamu: " + format(g));
    }

    /** Menampilkan pesan sukses setelah menghapus tamu. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus tamu.");
    }

    /** Menampilkan pesan gagal saat menghapus tamu. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus tamu dengan ID: %d.%n", id);
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
