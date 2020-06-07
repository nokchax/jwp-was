package webserver.processor;

import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutils.HttpRequestGenerator;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("컨트롤러 요청을 모아둔 프로세서 테스트")
class ControllerProcessorTest {
    private final ControllerProcessor controllerProcessor = new ControllerProcessor();

    @ParameterizedTest
    @MethodSource
    @DisplayName("isMatch 로 controller에 존재하는 요청들이 잘 걸러지는지")
    void isMatch(final HttpRequest httpRequest, final boolean expected) {
        assertThat(controllerProcessor.isMatch(httpRequest)).isEqualTo(expected);
    }

    private static Stream<Arguments> isMatch() throws IOException {
        return Stream.of(
                Arguments.of(HttpRequestGenerator.init("POST /user/login HTTP/1.1"), true),
                Arguments.of(HttpRequestGenerator.init("GET /user/login HTTP/1.1"), true),
                Arguments.of(HttpRequestGenerator.init("GET /user/list HTTP/1.1"), true),
                Arguments.of(HttpRequestGenerator.init("GET /not/exist HTTP/1.1"), false)
        );
    }
}
