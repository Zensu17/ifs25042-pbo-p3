package framework.view;

import adapter.presenter.FinancePresenter;
import domain.entity.SortOption;
import domain.entity.TransactionType;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.FinanceUseCase;

/** Tampilan konsol catatan keuangan: hanya interaksi user, tanpa logika bisnis. */
public class FinanceView {
    private final FinanceUseCase financeUseCase;
    private final FinancePresenter financePresenter;

    public FinanceView(FinanceUseCase financeUseCase, FinancePresenter financePresenter) {
        this.financeUseCase = financeUseCase;
        this.financePresenter = financePresenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            financePresenter.showTransactions(financeUseCase.getAllTransactions(), financeUseCase.getBalance());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> addTransaction(TransactionType.INCOME);
                case "2" -> addTransaction(TransactionType.EXPENSE);
                case "3" -> searchTransaction();
                case "4" -> sortTransaction();
                case "5" -> financePresenter.showBalance(financeUseCase.getBalance());
                case "6" -> removeTransaction();
                case "x" -> running = false;
                default -> financePresenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    /** Mencetak opsi menu ke layar. */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah Pemasukan");
        System.out.println("2. Tambah Pengeluaran");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Lihat Saldo");
        System.out.println("6. Hapus");
        System.out.println("x. Keluar");
    }

    private void addTransaction(TransactionType type) {
        System.out.println(type == TransactionType.INCOME ? "[Tambah Pemasukan]" : "[Tambah Pengeluaran]");
        String description = InputUtil.input("Keterangan (x Jika Batal)");
        if (description.equals("x")) {
            return;
        }
        String strAmount = InputUtil.input("Jumlah");

        try {
            financePresenter.showAddSuccess(financeUseCase.addTransaction(description, parseAmount(strAmount), type));
        } catch (ValidationException e) {
            financePresenter.showError(e.getMessage());
        }
    }

    /** Form cari transaksi berdasarkan kata kunci. */
    private void searchTransaction() {
        System.out.println("[Cari Transaksi]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (!keyword.equals("x")) {
            financePresenter.showSearchResults(financeUseCase.searchTransactions(keyword), keyword);
        }
    }

    /** Form urutkan transaksi berdasarkan pilihan user. */
    private void sortTransaction() {
        System.out.println("[Urutkan Transaksi]");
        System.out.println("1. Jumlah (Terkecil)");
        System.out.println("2. Jumlah (Terbesar)");
        System.out.println("3. Pemasukan Dulu");
        System.out.println("4. Pengeluaran Dulu");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        System.out.println();
        if (input.equals("x")) {
            return;
        }

        SortOption option = switch (input) {
            case "1" -> SortOption.AMOUNT_ASC;
            case "2" -> SortOption.AMOUNT_DESC;
            case "3" -> SortOption.INCOME_FIRST;
            case "4" -> SortOption.EXPENSE_FIRST;
            default -> null;
        };
        if (option == null) {
            financePresenter.showInvalidSortOption();
            return;
        }
        financePresenter.showSortedTransactions(financeUseCase.sortTransactions(option));
    }

    /** Form hapus transaksi berdasarkan ID. */
    private void removeTransaction() {
        System.out.println("[Hapus Transaksi]");
        String strId = InputUtil.input("ID Transaksi (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            financeUseCase.removeTransaction(parseId(strId));
            financePresenter.showRemoveSuccess();
        } catch (ValidationException e) {
            financePresenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            financePresenter.showRemoveFailed(e.getId());
        }
    }

    /** Mengonversi input string menjadi jumlah (angka bulat). */
    private long parseAmount(String value) throws ValidationException {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Jumlah tidak valid!");
        }
    }

    /**
     * Mengonversi input string menjadi ID.
     *
     * @return ID jika valid
     * @throws ValidationException jika string bukan format angka
     */
    private int parseId(String value) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("ID harus berupa angka numerik");
        }
    }
}
