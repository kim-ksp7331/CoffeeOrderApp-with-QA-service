package orderapp.coffeeorder.coffee.mapper;

import orderapp.coffeeorder.coffee.Coffee;
import orderapp.coffeeorder.coffee.CoffeeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {
    Coffee coffeePostDTOToCoffee(CoffeeDTO.Post coffeePostDTO);

    Coffee coffeePatchDTOToCoffee(CoffeeDTO.Patch coffeePatchDTO);
    CoffeeDTO.Response coffeeToCoffeeResponseDTO(Coffee coffee);

    List<CoffeeDTO.Response> coffeesToCoffeeResponseDTOs(List<Coffee> coffees);
}
