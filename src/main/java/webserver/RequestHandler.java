package webserver;

import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.SessionManager;
import webserver.processor.Processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final SessionManager sessionManager;
    private final Processors processors;

    public RequestHandler(final Socket connectionSocket,
                          final SessionManager sessionManager,
                          final Processors processors) {
        this.connection = connectionSocket;
        this.sessionManager = sessionManager;
        this.processors = processors;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.readRawRequest(in);
            HttpResponse httpResponse = HttpResponse.init();
            logger.debug("{}", httpRequest.toString());

            sessionManager.loadSession(httpRequest, httpResponse); // using filter?
            processors.process(httpRequest, httpResponse);

            httpResponse.writeResponse(out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
