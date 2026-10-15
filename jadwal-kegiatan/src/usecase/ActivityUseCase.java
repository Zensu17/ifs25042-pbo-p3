package usecase;

import domain.entity.Activity;
import domain.entity.Day;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IActivityRepository;
import java.util.List;
import java.util.regex.Pattern;

/** Logika bisnis jadwal kegiatan. Tanpa I/O; melempar checked domain exception. */
public class ActivityUseCase {
    private static final Pattern TIME_FORMAT = Pattern.compile("([01]\\d|2[0-3]):[0-5]\\d");

    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IActivityRepository activityRepository;

    public ActivityUseCase(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /** Mengambil semua kegiatan yang tersedia. */
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /** Menambahkan kegiatan baru setelah validasi. */
    public Activity addActivity(String title, String day, String time) throws ValidationException {
        validateTitle(title);
        Day parsedDay = Day.parse(day);
        validateTime(time);
        return activityRepository.save(title.trim(), parsedDay, time.trim());
    }

    /** Update parsial: parameter null berarti field tidak diubah. Semua validasi dilakukan sebelum mutasi. */
    public Activity updateActivity(int id, String title, String day, String time)
            throws EntityNotFoundException, ValidationException {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));

        if (title != null) {
            validateTitle(title);
        }
        Day parsedDay = day == null ? null : Day.parse(day);
        if (time != null) {
            validateTime(time);
        }

        if (title != null) {
            activity.changeTitle(title.trim());
        }
        if (parsedDay != null) {
            activity.changeDay(parsedDay);
        }
        if (time != null) {
            activity.changeTime(time.trim());
        }
        activityRepository.update(activity);
        return activity;
    }

    /** Mencari kegiatan berdasarkan kata kunci (case-insensitive). */
    public List<Activity> searchActivities(String keyword) {
        String key = keyword.toLowerCase();
        return activityRepository.findBy(a -> a.getTitle().toLowerCase().contains(key));
    }

    /** Mengurutkan kegiatan sesuai kriteria SortOption yang dipilih. */
    public List<Activity> sortActivities(SortOption option) {
        return activityRepository.findAll().stream().sorted(option.comparator()).toList();
    }

    /** Menghapus kegiatan berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeActivity(int id) throws EntityNotFoundException {
        if (!activityRepository.deleteById(id)) {
            throw new EntityNotFoundException(id);
        }
    }

    /** Memvalidasi input; melempar ValidationException jika tidak valid. */
    private void validateTitle(String title) throws ValidationException {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Judul kegiatan tidak boleh kosong!");
        }
    }

    /** Memvalidasi input; melempar ValidationException jika tidak valid. */
    private void validateTime(String time) throws ValidationException {
        if (time == null || !TIME_FORMAT.matcher(time.trim()).matches()) {
            throw new ValidationException("Waktu tidak valid (format HH:mm)!");
        }
    }
}
