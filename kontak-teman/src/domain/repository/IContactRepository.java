package domain.repository;

import domain.entity.Contact;

/** Port spesifik Contact; menambah pembuatan kontak dengan ID otomatis. */
public interface IContactRepository extends IRepository<Contact, Integer> {
    Contact save(String name, String phone, String email);
}
