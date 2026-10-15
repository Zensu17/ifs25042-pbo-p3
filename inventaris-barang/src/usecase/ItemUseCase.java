package usecase;

import domain.entity.Item;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IItemRepository;
import java.util.List;

/** Logika bisnis inventaris barang. Tanpa I/O; melempar checked domain exception. */
public class ItemUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IItemRepository itemRepository;

    public ItemUseCase(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /** Mengambil semua barang yang tersedia. */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /** Menambahkan barang baru setelah validasi. */
    public Item addItem(String name, int quantity, String category) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Nama barang tidak boleh kosong!");
        }
        validateQuantity(quantity);
        if (category == null || category.isBlank()) {
            throw new ValidationException("Kategori tidak boleh kosong!");
        }
        return itemRepository.save(name.trim(), quantity, category.trim());
    }

    /** Mengubah stok barang; mengembalikan barang yang sudah diperbarui. */
    public Item updateStock(int id, int quantity) throws EntityNotFoundException, ValidationException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        validateQuantity(quantity);
        item.changeQuantity(quantity);
        itemRepository.update(item);
        return item;
    }

    public Item updateStock(int id, String quantity) throws EntityNotFoundException, ValidationException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (quantity.isBlank()) {
            return item;
        }
        try {
            return updateStock(id, Integer.parseInt(quantity.trim()));
        } catch (NumberFormatException e) {
            throw new ValidationException("Jumlah stok tidak valid!");
        }
    }

    /** Mencari barang berdasarkan kata kunci (case-insensitive). */
    public List<Item> searchItems(String keyword) {
        String key = keyword.toLowerCase();
        return itemRepository.findBy(i -> i.getName().toLowerCase().contains(key));
    }

    /** Mengurutkan barang sesuai kriteria SortOption yang dipilih. */
    public List<Item> sortItems(SortOption option) {
        return itemRepository.findAll().stream().sorted(option.comparator()).toList();
    }

    /** Menghapus barang berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeItem(int id) throws EntityNotFoundException {
        if (!itemRepository.deleteById(id)) {
            throw new EntityNotFoundException(id);
        }
    }

    /** Memvalidasi input; melempar ValidationException jika tidak valid. */
    private void validateQuantity(int quantity) throws ValidationException {
        if (quantity <= 0) {
            throw new ValidationException("Jumlah stok tidak valid!");
        }
    }
}
