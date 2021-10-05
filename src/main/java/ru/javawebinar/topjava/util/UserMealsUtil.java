package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreamsWithCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreamsWithAnotherCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDateMap = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>(meals.size());
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesPerDateMap.put(date, caloriesPerDateMap.getOrDefault(date, 0) + meal.getCalories());
        }
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(
                        mealDateTime,
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesPerDateMap.get(mealDateTime.toLocalDate()) > caloriesPerDay
                ));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, UserMealWithExcess.CaloriesPerDayAccumulator> caloriesPerDateMap = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>(meals.size());
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            UserMealWithExcess.CaloriesPerDayAccumulator accumulator = caloriesPerDateMap.get(mealDateTime.toLocalDate());
            if (accumulator == null) {
                accumulator = new UserMealWithExcess.CaloriesPerDayAccumulator(caloriesPerDay);
                caloriesPerDateMap.put(mealDateTime.toLocalDate(), accumulator);
            }
            accumulator.addCalories(meal.getCalories());
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(
                        mealDateTime,
                        meal.getDescription(),
                        meal.getCalories(),
                        accumulator
                ));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDateMap = meals.stream()
                .collect(Collectors.toMap(m -> m.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(
                        m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        caloriesPerDateMap.get(m.getDateTime().toLocalDate()) > caloriesPerDay
                ))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsWithCustomCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(getUserMealsWithExcessCollector(startTime, endTime, caloriesPerDay));
    }

    public static List<UserMealWithExcess> filteredByStreamsWithAnotherCustomCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(getAnotherUserMealsWithExcessCollector(startTime, endTime, caloriesPerDay));
    }

    private static Collector<UserMeal, AbstractMap.SimpleImmutableEntry<HashMap<LocalDate, UserMealWithExcess.CaloriesPerDayAccumulator>, ArrayList<UserMealWithExcess>>, List<UserMealWithExcess>> getUserMealsWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return Collector.of(
                () -> new AbstractMap.SimpleImmutableEntry<>(new HashMap<>(), new ArrayList<>()),
                (supplier, meal) -> {
                    LocalDateTime mealDateTime = meal.getDateTime();
                    HashMap<LocalDate, UserMealWithExcess.CaloriesPerDayAccumulator> caloriesPerDateMap = supplier.getKey();
                    UserMealWithExcess.CaloriesPerDayAccumulator accumulator = caloriesPerDateMap.get(mealDateTime.toLocalDate());
                    if (accumulator == null) {
                        accumulator = new UserMealWithExcess.CaloriesPerDayAccumulator(caloriesPerDay);
                        caloriesPerDateMap.put(mealDateTime.toLocalDate(), accumulator);
                    }
                    accumulator.addCalories(meal.getCalories());
                    if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                        supplier.getValue().add(new UserMealWithExcess(
                                mealDateTime,
                                meal.getDescription(),
                                meal.getCalories(),
                                accumulator
                        ));
                    }
                },
                (first, second) -> {
                    first.getKey().putAll(second.getKey());
                    first.getValue().addAll(second.getValue());
                    return first;
                },
                AbstractMap.SimpleImmutableEntry::getValue
        );
    }

    private static Collector<UserMeal, AbstractMap.SimpleImmutableEntry<HashMap<LocalDate, Integer>, ArrayList<UserMeal>>, List<UserMealWithExcess>> getAnotherUserMealsWithExcessCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return Collector.of(
                () -> new AbstractMap.SimpleImmutableEntry<>(new HashMap<>(), new ArrayList<>()),
                (supplier, meal) -> {
                    LocalDateTime mealDateTime = meal.getDateTime();
                    HashMap<LocalDate, Integer> caloriesPerDateMap = supplier.getKey();
                    caloriesPerDateMap.put(mealDateTime.toLocalDate(), caloriesPerDateMap.getOrDefault(mealDateTime.toLocalDate(), 0) + meal.getCalories());
                    if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                        supplier.getValue().add(meal);
                    }
                },
                (first, second) -> {
                    first.getKey().putAll(second.getKey());
                    first.getValue().addAll(second.getValue());
                    return first;
                },
                supplier -> supplier.getValue().stream()
                        .map(m -> new UserMealWithExcess(
                                m.getDateTime(),
                                m.getDescription(),
                                m.getCalories(),
                                supplier.getKey().get(m.getDateTime().toLocalDate()) > caloriesPerDay
                        ))
                        .collect(Collectors.toList())
        );
    }
}
