package domain.repository;

import domain.entity.Activity;
import domain.entity.Day;

/** Port spesifik Activity; menambah pembuatan kegiatan dengan ID otomatis. */
public interface IActivityRepository extends IRepository<Activity, Integer> {
    Activity save(String title, Day day, String time);
}
