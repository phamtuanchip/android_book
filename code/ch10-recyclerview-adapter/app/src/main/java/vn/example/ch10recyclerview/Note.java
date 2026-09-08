package vn.example.ch10recyclerview;

/** Model đơn giản — một ghi chú có tiêu đề và thời điểm tạo. */
public class Note {
    private final long id;
    private final String title;
    private final String createdAt;

    public Note(long id, String title, String createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
