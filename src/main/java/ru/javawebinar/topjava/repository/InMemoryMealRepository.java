package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMealRepository implements IMealRepository {
    private static final AtomicLong COUNTER = new AtomicLong(1);

    private static InMemoryMealRepository singleton;

    private final ConcurrentMap<Long, Meal> repository;

    private InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
    }

    public static IMealRepository getInstance() {
        if (singleton == null) {
            singleton = new InMemoryMealRepository();
        }
        return singleton;
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(COUNTER.getAndAdd(1));
        repository.put(meal.getId(), meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        repository.replace(meal.getId(), meal);
    }

    @Override
    public Meal getMealById(long mealId) {
        return repository.get(mealId);
    }

    @Override
    public void deleteMeal(long mealId) {
        repository.remove(mealId);
    }

    @Override
    public List<Meal> getAllMeal() {
        return new ArrayList<>(repository.values());
    }
}
