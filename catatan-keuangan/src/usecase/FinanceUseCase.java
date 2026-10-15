package usecase;

import domain.entity.SortOption;
import domain.entity.Transaction;
import domain.entity.TransactionType;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.ITransactionRepository;
import java.util.List;

/** Logika bisnis catatan keuangan. Tanpa I/O; melempar checked domain exception. */
public class FinanceUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final ITransactionRepository financeRepository;

    public FinanceUseCase(ITransactionRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    /** Mengambil semua transaksi yang tersedia. */
    public List<Transaction> getAllTransactions() {
        return financeRepository.findAll();
    }

    public Transaction addTransaction(String description, long amount, TransactionType type)
            throws ValidationException {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Keterangan tidak boleh kosong!");
        }
        if (amount <= 0) {
            throw new ValidationException("Jumlah tidak valid!");
        }
        return financeRepository.save(description.trim(), amount, type);
    }

    /** Menghapus transaksi berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeTransaction(int id) throws EntityNotFoundException {
        if (!financeRepository.deleteById(id)) {
            throw new EntityNotFoundException(id);
        }
    }

    /** Mencari transaksi berdasarkan kata kunci (case-insensitive). */
    public List<Transaction> searchTransactions(String keyword) {
        String key = keyword.toLowerCase();
        return financeRepository.findBy(t -> t.getDescription().toLowerCase().contains(key));
    }

    /** Mengurutkan transaksi sesuai kriteria SortOption yang dipilih. */
    public List<Transaction> sortTransactions(SortOption option) {
        return financeRepository.findAll().stream().sorted(option.comparator()).toList();
    }

    /** Saldo = total pemasukan - total pengeluaran. */
    public long getBalance() {
        return financeRepository.findAll().stream()
                .mapToLong(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : -t.getAmount())
                .sum();
    }
}
