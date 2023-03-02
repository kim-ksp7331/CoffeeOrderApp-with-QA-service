package orderapp.coffeeorder.coffee;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;
    private final CustomBeanUtils<Coffee> beanUtils;

    public Coffee createCoffee(Coffee coffee) {
        verifyExistsCoffee(coffee.getCoffeeCode());
        return coffeeRepository.save(coffee);
    }

    public Coffee updateCoffee(Coffee coffee) {
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId());
        Coffee updatedCoffee = beanUtils.copyNonNullProperties(coffee, findCoffee);
        return coffeeRepository.save(updatedCoffee);
    }

    public Coffee findCoffee(long coffeeId) {
        return findVerifiedCoffee(coffeeId);
    }

    public Page<Coffee> findCoffees(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("coffeeId").descending());
        return coffeeRepository.findAll(pageRequest);
    }

    public void deleteCoffee(long coffeeId) {
        Coffee findCoffee = findVerifiedCoffee(coffeeId);
        coffeeRepository.delete(findCoffee);
    }

    public Coffee findVerifiedCoffee(long coffeeId) {
        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);
        return optionalCoffee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
    }

    private void verifyExistsCoffee(String coffeeCode) {
        Optional<Coffee> optionalCoffee = coffeeRepository.findByCoffeeCode(coffeeCode);
        if (optionalCoffee.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS);
        }
    }
}
