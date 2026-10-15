package domain.repository;

import domain.entity.Guest;

/** Port spesifik Guest; menambah pembuatan tamu dengan ID otomatis. */
public interface IGuestRepository extends IRepository<Guest, Integer> {
    Guest save(String name, String purpose);
}
