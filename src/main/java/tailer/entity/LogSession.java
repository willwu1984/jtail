package tailer.entity;

import org.apache.commons.io.input.Tailer;

/**
 * Created by willwu on 16-2-29.
 */
public class LogSession {
    private String id;
    private Tailer tailer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tailer getTailer() {
        return tailer;
    }

    public void setTailer(Tailer tailer) {
        this.tailer = tailer;
    }
}