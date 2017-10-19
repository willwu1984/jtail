package tailer;

import org.apache.commons.io.input.Tailer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tailer.entity.LogSession;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by willwu on 16-2-26.
 */
public class WebsocketEndPoint extends TextWebSocketHandler {
    private static Logger log = LoggerFactory.getLogger(WebsocketEndPoint.class);
    private Map<String, LogSession> requestMap = new HashMap<>();

    private String logDir;

    public WebsocketEndPoint(String logDir) {
        this.logDir = logDir;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (!requestMap.containsKey(session.getId())) {
            return;
        }
        LogSession request = requestMap.get(session.getId());
        if (request.getTailer() != null) {
            log.info("tailer closed");
            request.getTailer().stop();
        }
        requestMap.remove(session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<NameValuePair> pairList = URLEncodedUtils.parse(session.getUri(), "UTF-8");
        if (pairList.size() == 0 || !pairList.get(0).getName().equals("id")) {
            session.sendMessage(new TextMessage("invalid uri"));
            return;
        }
        String id = pairList.get(0).getValue();
        String filepath = logDir + id;
        log.info(filepath);
        File file = new File(filepath);
        Tailer tailer = new Tailer(file, new LogTailerListener(session));
        LogSession logSession = new LogSession();
        logSession.setId(session.getId());
        logSession.setTailer(tailer);
        requestMap.put(session.getId(),logSession);
        new Thread(tailer).start();
    }
}
