package domain.entity;

/** Entity kegiatan: judul, hari, dan waktu (format HH:mm). */
public class Activity {
    private final int id;
    private String title;
    private Day day;
    private String time;

    public Activity(int id, String title, Day day, String time) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Day getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDay(Day day) {
        this.day = day;
    }

    public void changeTime(String time) {
        this.time = time;
    }
}
