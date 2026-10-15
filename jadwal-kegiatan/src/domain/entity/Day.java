package domain.entity;

import domain.exception.ValidationException;

/** Hari dalam seminggu, berurutan Senin-Minggu (urutan enum dipakai untuk pengurutan). */
public enum Day {
    SENIN("Senin"),
    SELASA("Selasa"),
    RABU("Rabu"),
    KAMIS("Kamis"),
    JUMAT("Jumat"),
    SABTU("Sabtu"),
    MINGGU("Minggu");

    private final String label;

    Day(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    /** Mengonversi teks (case-insensitive) menjadi Day. */
    public static Day parse(String text) throws ValidationException {
        for (Day day : values()) {
            if (day.label.equalsIgnoreCase(text.trim())) {
                return day;
            }
        }
        throw new ValidationException("Hari tidak valid!");
    }
}
