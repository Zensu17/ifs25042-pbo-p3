package adapter.presenter;

import domain.entity.Transaction;
import domain.entity.TransactionType;
import java.util.List;

/** Memformat data transaksi menjadi output layar. */
public class FinancePresenter {
    /** Memformat satu data menjadi baris teks untuk ditampilkan. */
    private String format(Transaction t) {
        String type = t.getType() == TransactionType.INCOME ? "Pemasukan" : "Pengeluaran";
        return String.format("[%d] %s | Rp %d | [%s]", t.getId(), t.getDescription(), t.getAmount(), type);
    }

    /** Helper umum untuk menampilkan daftar; menampilkan pesan kosong jika tidak ada data. */
    private void printList(List<Transaction> list, String header, String emptyMessage) {
        System.out.println(header);
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        list.forEach(t -> System.out.println(format(t)));
    }

    /** Daftar transaksi beserta saldo. */
    public void showTransactions(List<Transaction> list, long balance) {
        printList(list, "Daftar Transaksi:", "- Belum ada transaksi!");
        System.out.printf("Saldo: Rp %d%n", balance);
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Transaction> list, String keyword) {
        printList(list, "Hasil Pencarian: \"" + keyword + "\"", "- Transaksi tidak ditemukan!");
    }

    /** Menampilkan daftar yang sudah diurutkan. */
    public void showSortedTransactions(List<Transaction> list) {
        printList(list, "Daftar Transaksi (Terurut):", "- Belum ada transaksi!");
    }

    /** Menampilkan saldo saat ini. */
    public void showBalance(long balance) {
        System.out.printf("Saldo saat ini: Rp %d%n", balance);
    }

    /** Menampilkan pesan sukses setelah menambah transaksi. */
    public void showAddSuccess(Transaction t) {
        System.out.println("Berhasil menambah transaksi: " + format(t));
    }

    /** Menampilkan pesan sukses setelah menghapus transaksi. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus transaksi.");
    }

    /** Menampilkan pesan gagal saat menghapus transaksi. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus transaksi dengan ID: %d.%n", id);
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
