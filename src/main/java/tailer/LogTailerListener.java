package tailer;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by willwu on 16-2-29.
 */
public class LogTailerListener extends TailerListenerAdapter {
    private WebSocketSession session;
    private static Logger log = LoggerFactory.getLogger(LogTailerListener.class);

    public LogTailerListener(WebSocketSession session){
        this.session = session;
    }

    private String rebuildUTF8String(String line) {
        int len = line.length();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) line.charAt(i);
        }
        return new String(bytes, Charsets.UTF_8);
    }

    @Override
    public void handle(String line) {
        try {
            session.sendMessage(new TextMessage(rebuildUTF8String(line)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
