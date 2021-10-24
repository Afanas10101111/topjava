package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int START_MEAL_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final Meal userMeal02;
    public static final Meal userMeal03;
    public static final Meal userMeal04;
    public static final Meal userMeal05;
    public static final Meal userMeal06;
    public static final Meal userMeal07;
    public static final Meal userMeal08;

    public static final Meal adminMeal09;
    public static final Meal adminMeal10;
    public static final Meal adminMeal11;

    public static final List<Meal> referenceSortedUserMeals;

    static {
        int mealCounter = 0;
        userMeal02 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        userMeal03 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        userMeal04 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        userMeal05 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        userMeal06 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        userMeal07 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 31, 13, 0), "Обед", 500);
        userMeal08 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 31, 20, 0), "Ужин", 410);

        adminMeal09 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        adminMeal10 = new Meal(START_MEAL_ID + mealCounter++, LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        adminMeal11 = new Meal(START_MEAL_ID + mealCounter, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Ужин", 500);

        referenceSortedUserMeals = Stream.of(userMeal02, userMeal03, userMeal04, userMeal05, userMeal06, userMeal07, userMeal08)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.now(), "Some new meal", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal02);
        updated.setDescription("Some updated meal");
        updated.setDateTime(LocalDateTime.now());
        updated.setCalories(4444);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
