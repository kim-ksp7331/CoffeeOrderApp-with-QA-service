package orderapp.coffeeorder.coffee;

import orderapp.coffeeorder.coffee.mapper.CoffeeMapper;
import orderapp.coffeeorder.response.MultiResponseDTO;
import orderapp.coffeeorder.response.SingleResponseDTO;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/coffees")
@Validated
public class CoffeeController {
    private final static String COFFEE_DEFAULT_URL = "/coffees";
    private final CoffeeService coffeeService;
    private final CoffeeMapper mapper;

    public CoffeeController(CoffeeService coffeeService, CoffeeMapper mapper) {
        this.coffeeService = coffeeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> postCoffee(@Valid @RequestBody CoffeeDTO.Post coffeePostDTO) {
        Coffee coffee = coffeeService.createCoffee(mapper.coffeePostDTOToCoffee(coffeePostDTO));
        URI location = UriCreator.createUri(COFFEE_DEFAULT_URL, coffee.getCoffeeId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity<?> patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId,
                                         @Valid @RequestBody CoffeeDTO.Patch coffeePatchDTO) {
        coffeePatchDTO.setCoffeeId(coffeeId);
        Coffee coffee = coffeeService.updateCoffee(mapper.coffeePatchDTOToCoffee(coffeePatchDTO));
        CoffeeDTO.Response response = mapper.coffeeToCoffeeResponseDTO(coffee);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity<?> getCoffee(@PathVariable("coffee-id") @Positive long coffeeId) {
        CoffeeDTO.Response response = mapper.coffeeToCoffeeResponseDTO(coffeeService.findCoffee(coffeeId));
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCoffees(@RequestParam @Positive int page, @RequestParam @Positive int size) {
        Page<Coffee> coffeePage = coffeeService.findCoffees(page - 1, size);
        List<Coffee> coffees = coffeePage.getContent();
        return new ResponseEntity<>(
                new MultiResponseDTO<>(mapper.coffeesToCoffeeResponseDTOs(coffees), coffeePage), HttpStatus.OK);
    }

    @DeleteMapping("/{coffee-id}")
    public ResponseEntity<?> deleteCoffee(@PathVariable("coffee-id") long coffeeId) {
        coffeeService.deleteCoffee(coffeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
