package domain.entity;

import java.util.Comparator;

/** Kriteria pengurutan transaksi; setiap opsi membawa comparator-nya sendiri. */
public enum SortOption {
    AMOUNT_DESC(Comparator.comparingLong(Transaction::getAmount).reversed()),
    AMOUNT_ASC(Comparator.comparingLong(Transaction::getAmount)),
    INCOME_FIRST(Comparator.comparing(Transaction::getType)),
    EXPENSE_FIRST(Comparator.comparing(Transaction::getType).reversed());

    private final Comparator<Transaction> comparator;

    SortOption(Comparator<Transaction> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Transaction> comparator() {
        return comparator;
    }
}
