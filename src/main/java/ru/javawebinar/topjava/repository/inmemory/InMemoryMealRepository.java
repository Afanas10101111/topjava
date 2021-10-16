package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository;
    private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.forEach(m -> this.save(m.getUserId(), m));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (isMealBelongToUser(userId, meal)) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                repository.put(meal.getId(), meal);
                return meal;
            }
            // handle case: update, but not present in storage
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        return repository.values().removeIf(m -> isMealBelongToUser(userId, m) && m.getId() == id);
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        return isMealBelongToUser(userId, meal) ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(m -> isMealBelongToUser(userId, m))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isMealBelongToUser(int userId, Meal meal) {
        return meal != null && meal.getUserId() == userId;
    }
}

