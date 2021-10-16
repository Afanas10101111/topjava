package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    Meal add(Meal meal);

    Meal update(Meal meal);

    Meal getById(long mealId);

    void delete(long mealId);

    List<Meal> getAll();
}
