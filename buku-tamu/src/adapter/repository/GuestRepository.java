package adapter.repository;

import domain.entity.Guest;
import domain.repository.IGuestRepository;

/** Implementasi in-memory IGuestRepository. */
public class GuestRepository extends InMemoryRepository<Guest, Integer> implements IGuestRepository {
    /** Penghitung ID otomatis, bertambah setiap kali data baru disimpan. */
    private int idCounter = 0;

    public GuestRepository() {
        super(Guest::getId);
    }

    @Override
    public Guest save(String name, String purpose) {
        return save(new Guest(nextId(), name, purpose));
    }

    /** Menghasilkan ID unik berikutnya. */
    private int nextId() {
        return ++idCounter;
    }
}
