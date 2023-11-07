package util;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;

public final class JsonUtil {

    public static String fromPath(String path) throws IOException {
        return Files.readString(ResourceUtils.getFile("classpath:" + path).toPath());
    }
}
