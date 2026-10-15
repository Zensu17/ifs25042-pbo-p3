package domain.repository;

import domain.entity.Item;

/** Port spesifik Item; menambah pembuatan barang dengan ID otomatis. */
public interface IItemRepository extends IRepository<Item, Integer> {
    Item save(String name, int quantity, String category);
}
