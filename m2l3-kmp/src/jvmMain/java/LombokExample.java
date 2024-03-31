import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class LombokExample {

    // Обратите внимание: org.jetbrains.annotations.NotNull
    @NotNull
    private String str;
    private int i;
    @Nullable
    private Integer j;

}
