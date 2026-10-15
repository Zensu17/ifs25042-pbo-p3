package framework.view;

import adapter.presenter.ActivityPresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.ActivityUseCase;

/** Tampilan konsol jadwal kegiatan: hanya interaksi user, tanpa logika bisnis. */
public class ActivityView {
    private final ActivityUseCase activityUseCase;
    private final ActivityPresenter activityPresenter;

    public ActivityView(ActivityUseCase activityUseCase, ActivityPresenter activityPresenter) {
        this.activityUseCase = activityUseCase;
        this.activityPresenter = activityPresenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            activityPresenter.showActivities(activityUseCase.getAllActivities());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> addActivity();
                case "2" -> updateActivity();
                case "3" -> searchActivity();
                case "4" -> sortActivity();
                case "5" -> removeActivity();
                case "x" -> running = false;
                default -> activityPresenter.showInvalidChoice();
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

    private void addActivity() {
        System.out.println("[Menambah Kegiatan]");
        String title = InputUtil.input("Judul (x Jika Batal)");
        if (title.equals("x")) {
            return;
        }
        String day = InputUtil.input("Hari (x Jika Batal)");
        if (day.equals("x")) {
            return;
        }
        String time = InputUtil.input("Waktu (x Jika Batal)");
        if (time.equals("x")) {
            return;
        }

        try {
            activityPresenter.showAddSuccess(activityUseCase.addActivity(title, day, time));
        } catch (ValidationException e) {
            activityPresenter.showError(e.getMessage());
        }
    }

    /** Form ubah kegiatan. */
    private void updateActivity() {
        System.out.println("[Mengubah Kegiatan]");
        String strId = InputUtil.input("ID Kegiatan yang diubah (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        int id;
        try {
            id = parseId(strId);
        } catch (ValidationException e) {
            activityPresenter.showInvalidId();
            return;
        }

        // Kosong berarti field tidak diubah (null)
        String title = blankToNull(InputUtil.input("Judul Baru (Kosongkan jika tidak ingin mengubah)"));
        String day = blankToNull(InputUtil.input("Hari Baru (Kosongkan jika tidak ingin mengubah)"));
        String time = blankToNull(InputUtil.input("Waktu Baru (Kosongkan jika tidak ingin mengubah)"));

        try {
            activityPresenter.showUpdateSuccess(activityUseCase.updateActivity(id, title, day, time));
        } catch (ValidationException e) {
            activityPresenter.showError(e.getMessage());
        } catch (EntityNotFoundException e) {
            activityPresenter.showUpdateFailed(e.getId());
        }
    }

    /** Form cari kegiatan berdasarkan kata kunci. */
    private void searchActivity() {
        System.out.println("[Mencari Kegiatan]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (!keyword.equals("x")) {
            activityPresenter.showSearchResults(activityUseCase.searchActivities(keyword), keyword);
        }
    }

    /** Form urutkan kegiatan berdasarkan pilihan user. */
    private void sortActivity() {
        System.out.println("[Mengurutkan Kegiatan]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Hari (Senin -> Minggu)");
        System.out.println("2. Waktu (Awal -> Akhir)");
        System.out.println("3. Judul (A-Z)");
        System.out.println("4. Judul (Z-A)");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        System.out.println();
        if (input.equals("x")) {
            return;
        }

        SortOption option = switch (input) {
            case "1" -> SortOption.DAY;
            case "2" -> SortOption.TIME;
            case "3" -> SortOption.TITLE_ASC;
            case "4" -> SortOption.TITLE_DESC;
            default -> null;
        };
        if (option == null) {
            activityPresenter.showInvalidSortOption();
            return;
        }
        activityPresenter.showSortedActivities(activityUseCase.sortActivities(option));
    }

    /** Form hapus kegiatan berdasarkan ID. */
    private void removeActivity() {
        System.out.println("[Menghapus Kegiatan]");
        String strId = InputUtil.input("[ID Kegiatan] yang dihapus (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            activityUseCase.removeActivity(parseId(strId));
            activityPresenter.showRemoveSuccess();
        } catch (ValidationException e) {
            activityPresenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            activityPresenter.showRemoveFailed(e.getId());
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
