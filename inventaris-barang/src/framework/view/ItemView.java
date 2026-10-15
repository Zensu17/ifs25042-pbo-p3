package framework.view;

import adapter.presenter.ItemPresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.ItemUseCase;

/** Tampilan konsol inventaris barang: hanya interaksi user, tanpa logika bisnis. */
public class ItemView {
    private final ItemUseCase itemUseCase;
    private final ItemPresenter itemPresenter;

    public ItemView(ItemUseCase itemUseCase, ItemPresenter itemPresenter) {
        this.itemUseCase = itemUseCase;
        this.itemPresenter = itemPresenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            itemPresenter.showItems(itemUseCase.getAllItems());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> addItem();
                case "2" -> updateStock();
                case "3" -> searchItem();
                case "4" -> sortItem();
                case "5" -> removeItem();
                case "x" -> running = false;
                default -> itemPresenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    /** Mencetak opsi menu ke layar. */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah");
        System.out.println("2. Ubah Stok");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    private void addItem() {
        System.out.println("[Menambah Barang]");
        String name = InputUtil.input("Nama (x Jika Batal)");
        if (name.equals("x")) {
            return;
        }
        String strQty = InputUtil.input("Jumlah");

        try {
            int quantity = parseQuantity(strQty);
            String category = InputUtil.input("Kategori (x Jika Batal)");
            if (category.equals("x")) {
                return;
            }
            itemPresenter.showAddSuccess(itemUseCase.addItem(name, quantity, category));
        } catch (ValidationException e) {
            itemPresenter.showError(e.getMessage());
        }
    }

    /** Form ubah barang. */
    private void updateStock() {
        System.out.println("[Mengubah Stok]");
        String strId = InputUtil.input("ID Barang yang diubah (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        int id;
        try {
            id = parseId(strId);
        } catch (ValidationException e) {
            itemPresenter.showInvalidId();
            return;
        }
        String strQty = InputUtil.input("Jumlah Baru (Kosongkan jika tidak ingin mengubah)");

        try {
            itemPresenter.showUpdateSuccess(itemUseCase.updateStock(id, strQty));
        } catch (ValidationException e) {
            itemPresenter.showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            itemPresenter.showUpdateFailed(e.getId());
        }
    }

    /** Form cari barang berdasarkan kata kunci. */
    private void searchItem() {
        System.out.println("[Mencari Barang]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (!keyword.equals("x")) {
            itemPresenter.showSearchResults(itemUseCase.searchItems(keyword), keyword);
        }
    }

    /** Form urutkan barang berdasarkan pilihan user. */
    private void sortItem() {
        System.out.println("[Mengurutkan Barang]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Nama (A-Z)");
        System.out.println("2. Nama (Z-A)");
        System.out.println("3. Jumlah (Terkecil -> Terbesar)");
        System.out.println("4. Jumlah (Terbesar -> Terkecil)");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        System.out.println();
        if (input.equals("x")) {
            return;
        }

        SortOption option = switch (input) {
            case "1" -> SortOption.NAME_ASC;
            case "2" -> SortOption.NAME_DESC;
            case "3" -> SortOption.QUANTITY_ASC;
            case "4" -> SortOption.QUANTITY_DESC;
            default -> null;
        };
        if (option == null) {
            itemPresenter.showInvalidSortOption();
            return;
        }
        itemPresenter.showSortedItems(itemUseCase.sortItems(option));
    }

    /** Form hapus barang berdasarkan ID. */
    private void removeItem() {
        System.out.println("[Menghapus Barang]");
        String strId = InputUtil.input("[ID Barang] yang dihapus (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            itemUseCase.removeItem(parseId(strId));
            itemPresenter.showRemoveSuccess();
        } catch (ValidationException e) {
            itemPresenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            itemPresenter.showRemoveFailed(e.getId());
        }
    }

    /** Mengonversi input string menjadi jumlah stok. */
    private int parseQuantity(String value) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Jumlah stok tidak valid!");
        }
    }

    /**
     * Mengonversi input string menjadi ID.
     *
     * @return ID jika valid
     * @throws ValidationException jika string bukan format angka
     */
    private int parseId(String value) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("ID harus berupa angka numerik");
        }
    }
}
