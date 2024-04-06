import org.jetbrains.annotations.Nullable;

public class JavaClassExampleNull {

    // Обратите внимание: org.jetbrains.annotations.NotNull
    @Nullable
    private String value;

    JavaClassExampleNull(@Nullable String arg) {
        value = arg;
    }

    void setValue(@Nullable String arg) {
        value = arg;
    }

    @Nullable
    String getValue() {
        return value;
    }
}
