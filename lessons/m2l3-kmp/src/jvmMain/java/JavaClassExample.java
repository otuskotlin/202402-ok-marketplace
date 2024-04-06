import org.jetbrains.annotations.NotNull;

public class JavaClassExample {

    // Обратите внимание: org.jetbrains.annotations.NotNull
    @NotNull
    private String value;

    JavaClassExample(@NotNull String arg) {
        value = arg;
    }

    void setValue(@NotNull String arg) {
        value = arg;
    }

    @NotNull
    String getValue() {
        return value;
    }
}
