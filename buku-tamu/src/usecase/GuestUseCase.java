package usecase;

import domain.entity.Guest;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IGuestRepository;
import java.util.List;

/** Logika bisnis buku tamu. Tanpa I/O; melempar checked domain exception. */
public class GuestUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IGuestRepository guestRepository;

    public GuestUseCase(IGuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    /** Mengambil semua tamu yang tersedia. */
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    /** Menambahkan tamu baru setelah validasi. */
    public Guest registerGuest(String name, String purpose) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Nama tamu tidak boleh kosong!");
        }
        if (purpose == null || purpose.isBlank()) {
            throw new ValidationException("Tujuan kunjungan tidak boleh kosong!");
        }
        return guestRepository.save(name.trim(), purpose.trim());
    }

    /** Pencarian berdasarkan nama (case-insensitive). */
    public List<Guest> searchGuests(String keyword) {
        String key = keyword.toLowerCase();
        return guestRepository.findBy(g -> g.getName().toLowerCase().contains(key));
    }

    /** Menghapus tamu berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeGuest(int id) throws EntityNotFoundException {
        if (!guestRepository.deleteById(id)) {
            throw new EntityNotFoundException(id);
        }
    }
}
