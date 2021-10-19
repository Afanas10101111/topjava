package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository;
    private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.forEach(m -> this.save(1, m));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            userMeals = new ConcurrentHashMap<>();
            repository.put(userId, userMeals);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.values().removeIf(m -> m.getId() == id);
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ? userMeals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ?
                userMeals.values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList()) :
                Collections.emptyList();
    }

    @Override
    public List<Meal> getAllFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllFilteredByDate");
        return getAll(userId).stream()
                .filter(m -> DateTimeUtil.isBetweenClosed(m.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

