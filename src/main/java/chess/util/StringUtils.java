package chess.util;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String text) {
        return text == null  || "".equals(text.trim());
    }
}
