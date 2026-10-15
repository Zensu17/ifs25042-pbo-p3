package adapter.presenter;

import domain.entity.Item;
import java.util.List;

/** Memformat data barang menjadi output layar. */
public class ItemPresenter {
    /** Memformat satu data menjadi baris teks untuk ditampilkan. */
    private String format(Item i) {
        return String.format("[%d] %s | Stok: %d | Kategori: %s", i.getId(), i.getName(), i.getQuantity(), i.getCategory());
    }

    /** Helper umum untuk menampilkan daftar; menampilkan pesan kosong jika tidak ada data. */
    private void printList(List<Item> items, String header, String emptyMessage) {
        System.out.println(header);
        if (items.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        items.forEach(i -> System.out.println(format(i)));
    }

    /** Menampilkan daftar semua barang. */
    public void showItems(List<Item> items) {
        printList(items, "Daftar Barang:", "- Data barang belum tersedia!");
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Item> items, String keyword) {
        printList(items, "Hasil Pencarian: \"" + keyword + "\"", "- Barang tidak ditemukan!");
    }

    /** Menampilkan daftar yang sudah diurutkan. */
    public void showSortedItems(List<Item> items) {
        printList(items, "Daftar Barang (Terurut):", "- Data barang belum tersedia!");
    }

    /** Menampilkan pesan sukses setelah menambah barang. */
    public void showAddSuccess(Item i) {
        System.out.println("Berhasil menambah barang: " + format(i));
    }

    /** Menampilkan pesan sukses setelah mengubah barang. */
    public void showUpdateSuccess(Item i) {
        System.out.println("Berhasil mengubah stok barang.");
    }

    /** Menampilkan pesan gagal saat mengubah barang. */
    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah stok barang dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan sukses setelah menghapus barang. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus barang.");
    }

    /** Menampilkan pesan gagal saat menghapus barang. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus barang dengan ID: %d.%n", id);
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
