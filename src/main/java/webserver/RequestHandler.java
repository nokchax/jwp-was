package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.reader.DefaultHttpRequestReader;
import webserver.reader.HttpRequestReader;
import webserver.writer.DefaultHttpResponseWriter;
import webserver.writer.HttpResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HttpRequestReader requestReader = new DefaultHttpRequestReader();
    private final HttpResponseWriter responseWriter = new DefaultHttpResponseWriter();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = requestReader.read(in);

            //filePath decider
            byte[] fileBytes = FileIoUtils.loadFileFromClasspath("./templates" + httpRequest.getPath());

            responseWriter.write(out, new HttpResponse());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
