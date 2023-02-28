package orderapp.coffeeorder.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingleResponseDTO<T> {
    private T data;
}
