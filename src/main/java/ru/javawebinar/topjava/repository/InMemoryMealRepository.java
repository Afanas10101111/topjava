package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMealRepository implements MealRepository {
    private final AtomicLong counter = new AtomicLong(1);

    private final Map<Long, Meal> repository;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(this::add);
    }

    @Override
    public Meal add(Meal meal) {
        Long id = meal.getId();
        if (id == null) {
            meal.setId(id = counter.getAndIncrement());
            repository.put(id, meal);
            return meal;
        }
        return null;
    }

    @Override
    public Meal update(Meal meal) {
        return repository.replace(meal.getId(), meal);
    }

    @Override
    public Meal getById(long mealId) {
        return repository.get(mealId);
    }

    @Override
    public void delete(long mealId) {
        repository.remove(mealId);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(repository.values());
    }
}
