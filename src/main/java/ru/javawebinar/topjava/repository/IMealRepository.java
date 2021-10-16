package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IMealRepository {
    void addMeal(Meal meal);

    void updateMeal(Meal meal);

    Meal getMealById(long mealId);

    void deleteMeal(long mealId);

    List<Meal> getAllMeal();
}
