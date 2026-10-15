package framework.view;

import adapter.presenter.GuestPresenter;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.GuestUseCase;

/** Tampilan konsol buku tamu: hanya interaksi user, tanpa logika bisnis. */
public class GuestView {
    private final GuestUseCase guestUseCase;
    private final GuestPresenter guestPresenter;

    public GuestView(GuestUseCase guestUseCase, GuestPresenter guestPresenter) {
        this.guestUseCase = guestUseCase;
        this.guestPresenter = guestPresenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            guestPresenter.showGuests(guestUseCase.getAllGuests());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> registerGuest();
                case "2" -> searchGuest();
                case "3" -> removeGuest();
                case "x" -> running = false;
                default -> guestPresenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    /** Mencetak opsi menu ke layar. */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Daftarkan");
        System.out.println("2. Cari");
        System.out.println("3. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah tamu baru. */
    private void registerGuest() {
        System.out.println("[Mendaftarkan Tamu]");
        String name = InputUtil.input("Nama (x Jika Batal)");
        if (name.equals("x")) {
            return;
        }
        String purpose = InputUtil.input("Tujuan Kunjungan (x Jika Batal)");
        if (purpose.equals("x")) {
            return;
        }

        try {
            guestPresenter.showRegisterSuccess(guestUseCase.registerGuest(name, purpose));
        } catch (ValidationException e) {
            guestPresenter.showError(e.getMessage());
        }
    }

    /** Form cari tamu berdasarkan kata kunci. */
    private void searchGuest() {
        System.out.println("[Mencari Tamu]");
        String keyword = InputUtil.input("Nama (x Jika Batal)");
        if (!keyword.equals("x")) {
            guestPresenter.showSearchResults(guestUseCase.searchGuests(keyword), keyword);
        }
    }

    /** Form hapus tamu berdasarkan ID. */
    private void removeGuest() {
        System.out.println("[Menghapus Tamu]");
        String strId = InputUtil.input("[ID Tamu] yang dihapus (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            guestUseCase.removeGuest(parseId(strId));
            guestPresenter.showRemoveSuccess();
        } catch (ValidationException e) {
            guestPresenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            guestPresenter.showRemoveFailed(e.getId());
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
