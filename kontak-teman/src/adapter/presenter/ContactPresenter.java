package adapter.presenter;

import domain.entity.Contact;
import java.util.List;

/** Memformat data kontak menjadi output layar. */
public class ContactPresenter {
    /** Memformat satu data menjadi baris teks untuk ditampilkan. */
    private String format(Contact c) {
        return String.format("[%d] %s | Telp: %s | Email: %s", c.getId(), c.getName(), c.getPhone(), c.getEmail());
    }

    /** Helper umum untuk menampilkan daftar; menampilkan pesan kosong jika tidak ada data. */
    private void printList(List<Contact> list, String header, String emptyMessage) {
        System.out.println(header);
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        list.forEach(c -> System.out.println(format(c)));
    }

    /** Menampilkan daftar semua kontak. */
    public void showContacts(List<Contact> list) {
        printList(list, "Daftar Kontak:", "- Data kontak belum tersedia!");
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Contact> list, String keyword) {
        printList(list, "Hasil Pencarian: \"" + keyword + "\"", "- Kontak tidak ditemukan!");
    }

    /** Menampilkan daftar yang sudah diurutkan. */
    public void showSortedContacts(List<Contact> list) {
        printList(list, "Daftar Kontak (Terurut):", "- Data kontak belum tersedia!");
    }

    /** Menampilkan pesan sukses setelah menambah kontak. */
    public void showAddSuccess(Contact c) {
        System.out.println("Berhasil menambah kontak: " + format(c));
    }

    /** Menampilkan pesan sukses setelah mengubah kontak. */
    public void showUpdateSuccess(Contact c) {
        System.out.println("Berhasil mengubah kontak.");
    }

    /** Menampilkan pesan gagal saat mengubah kontak. */
    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah kontak dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan sukses setelah menghapus kontak. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus kontak.");
    }

    /** Menampilkan pesan gagal saat menghapus kontak. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus kontak dengan ID: %d.%n", id);
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
