package domain.entity;

/** Entity satu transaksi keuangan (pemasukan atau pengeluaran). */
public class Transaction {
    private final int id;
    private final String description;
    private final long amount;
    private final TransactionType type;

    public Transaction(int id, String description, long amount, TransactionType type) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
