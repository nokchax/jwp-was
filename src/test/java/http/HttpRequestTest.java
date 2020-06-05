package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static http.RawRequestTest.HEADER;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요청에 대한 객체")
public class HttpRequestTest {

    @Test
    void parse() {
        RequestLine requestLine = RequestLineParser.parse("GET /users?name1=value1&name2=value2 HTTP/1.1");
        HttpRequest httpRequest = HttpRequest.init(requestLine, new RequestHeader(HEADER));

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/users");
        assertThat(httpRequest.getParameter("name1")).isEqualTo("value1");
        assertThat(httpRequest.getParameter("name2")).isEqualTo("value2");
        assertThat(httpRequest.getProtocol()).isEqualTo("HTTP");
        assertThat(httpRequest.getVersion()).isEqualTo("1.1");
    }
}
