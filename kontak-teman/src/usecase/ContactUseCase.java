package usecase;

import domain.entity.Contact;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IContactRepository;
import java.util.List;

/** Logika bisnis kontak teman. Tanpa I/O; melempar checked domain exception. */
public class ContactUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IContactRepository contactRepository;

    public ContactUseCase(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /** Mengambil semua kontak yang tersedia. */
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    /** Menambahkan kontak baru setelah validasi. */
    public Contact addContact(String name, String phone, String email) throws ValidationException {
        requireText(name, "Nama kontak tidak boleh kosong!");
        requireText(phone, "Nomor telepon tidak boleh kosong!");
        requireText(email, "Email tidak boleh kosong!");
        return contactRepository.save(name.trim(), phone.trim(), email.trim());
    }

    /** Update parsial: parameter null berarti field tidak diubah. */
    public Contact updateContact(int id, String name, String phone, String email)
            throws EntityNotFoundException, ValidationException {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));

        if (name != null) {
            requireText(name, "Nama kontak tidak boleh kosong!");
            contact.changeName(name.trim());
        }
        if (phone != null) {
            requireText(phone, "Nomor telepon tidak boleh kosong!");
            contact.changePhone(phone.trim());
        }
        if (email != null) {
            requireText(email, "Email tidak boleh kosong!");
            contact.changeEmail(email.trim());
        }
        contactRepository.update(contact);
        return contact;
    }

    /** Mencari kontak berdasarkan kata kunci (case-insensitive). */
    public List<Contact> searchContacts(String keyword) {
        String key = keyword.toLowerCase();
        return contactRepository.findBy(c -> c.getName().toLowerCase().contains(key));
    }

    /** Mengurutkan kontak sesuai kriteria SortOption yang dipilih. */
    public List<Contact> sortContacts(SortOption option) {
        return contactRepository.findAll().stream().sorted(option.comparator()).toList();
    }

    /** Menghapus kontak berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeContact(int id) throws EntityNotFoundException {
        if (!contactRepository.deleteById(id)) {
            throw new EntityNotFoundException(id);
        }
    }

    /** Memvalidasi input; melempar ValidationException jika tidak valid. */
    private void requireText(String value, String message) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }
}
