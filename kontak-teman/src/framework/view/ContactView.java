package framework.view;

import adapter.presenter.ContactPresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.ContactUseCase;

/** Tampilan konsol kontak teman: hanya interaksi user, tanpa logika bisnis. */
public class ContactView {
    private final ContactUseCase contactUseCase;
    private final ContactPresenter contactPresenter;

    public ContactView(ContactUseCase contactUseCase, ContactPresenter contactPresenter) {
        this.contactUseCase = contactUseCase;
        this.contactPresenter = contactPresenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            contactPresenter.showContacts(contactUseCase.getAllContacts());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> addContact();
                case "2" -> updateContact();
                case "3" -> searchContact();
                case "4" -> sortContact();
                case "5" -> removeContact();
                case "x" -> running = false;
                default -> contactPresenter.showInvalidChoice();
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
        System.out.println("2. Ubah");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    private void addContact() {
        System.out.println("[Menambah Kontak]");
        String name = InputUtil.input("Nama (x Jika Batal)");
        if (name.equals("x")) {
            return;
        }
        String phone = InputUtil.input("Telepon");
        String email = InputUtil.input("Email");

        try {
            contactPresenter.showAddSuccess(contactUseCase.addContact(name, phone, email));
        } catch (ValidationException e) {
            contactPresenter.showError(e.getMessage());
        }
    }

    /** Form ubah kontak. */
    private void updateContact() {
        System.out.println("[Mengubah Kontak]");
        String strId = InputUtil.input("ID Kontak yang diubah (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        int id;
        try {
            id = parseId(strId);
        } catch (ValidationException e) {
            contactPresenter.showInvalidId();
            return;
        }

        // Kosong berarti field tidak diubah (null)
        String name = blankToNull(InputUtil.input("Nama Baru (Kosongkan jika tidak ingin mengubah)"));
        String phone = blankToNull(InputUtil.input("Telepon Baru (Kosongkan jika tidak ingin mengubah)"));
        String email = blankToNull(InputUtil.input("Email Baru (Kosongkan jika tidak ingin mengubah)"));

        try {
            contactPresenter.showUpdateSuccess(contactUseCase.updateContact(id, name, phone, email));
        } catch (ValidationException e) {
            contactPresenter.showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            contactPresenter.showUpdateFailed(e.getId());
        }
    }

    /** Form cari kontak berdasarkan kata kunci. */
    private void searchContact() {
        System.out.println("[Mencari Kontak]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (!keyword.equals("x")) {
            contactPresenter.showSearchResults(contactUseCase.searchContacts(keyword), keyword);
        }
    }

    /** Form urutkan kontak berdasarkan pilihan user. */
    private void sortContact() {
        System.out.println("[Mengurutkan Kontak]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Nama (A-Z)");
        System.out.println("2. Nama (Z-A)");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        System.out.println();
        if (input.equals("x")) {
            return;
        }

        SortOption option = switch (input) {
            case "1" -> SortOption.NAME_ASC;
            case "2" -> SortOption.NAME_DESC;
            default -> null;
        };
        if (option == null) {
            contactPresenter.showInvalidSortOption();
            return;
        }
        contactPresenter.showSortedContacts(contactUseCase.sortContacts(option));
    }

    /** Form hapus kontak berdasarkan ID. */
    private void removeContact() {
        System.out.println("[Menghapus Kontak]");
        String strId = InputUtil.input("[ID Kontak] yang dihapus (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            contactUseCase.removeContact(parseId(strId));
            contactPresenter.showRemoveSuccess();
        } catch (ValidationException e) {
            contactPresenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            contactPresenter.showRemoveFailed(e.getId());
        }
    }

    /** Mengubah string kosong menjadi null (field tidak diubah). */
    private String blankToNull(String value) {
        return value.isBlank() ? null : value;
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
