package ru.javawebinar.topjava.config;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.IMealRepository;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class ProjectConstantsHolder {
    public static final int CALORIES_DAY_LIMIT = 2000;
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );
    public static final IMealRepository REPOSITORY = InMemoryMealRepository.getInstance();

    static {
        MEALS.forEach(REPOSITORY::addMeal);
    }
}
