package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int START_MEAL_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final Meal userMeal01 = new Meal(START_MEAL_ID, LocalDateTime.of(2021, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal userMeal02 = new Meal(START_MEAL_ID + 1, LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal userMeal03 = new Meal(START_MEAL_ID + 2, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal userMeal04 = new Meal(START_MEAL_ID + 3, LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0), "Еда на грани", 100);
    public static final Meal userMeal05 = new Meal(START_MEAL_ID + 4, LocalDateTime.of(2021, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userMeal06 = new Meal(START_MEAL_ID + 5, LocalDateTime.of(2021, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal userMeal07 = new Meal(START_MEAL_ID + 6, LocalDateTime.of(2021, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal adminMeal01 = new Meal(START_MEAL_ID + 7, LocalDateTime.of(2021, Month.JANUARY, 30, 10, 0), "Завтрак Админа", 500);
    public static final Meal adminMeal02 = new Meal(START_MEAL_ID + 8, LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед Админа", 1000);
    public static final Meal adminMeal03 = new Meal(START_MEAL_ID + 9, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Ужин Админа", 500);

    public static final List<Meal> referenceSortedUserMeals
            = Arrays.asList(userMeal07, userMeal06, userMeal05, userMeal04, userMeal03, userMeal02, userMeal01);
    public static final List<Meal> referenceFilteredSortedUserMeals = Arrays.asList(userMeal03, userMeal02, userMeal01);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2021, Month.JUNE, 24, 8, 8), "Some new meal", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal02);
        updated.setDescription("Some updated meal");
        updated.setDateTime(LocalDateTime.of(2021, Month.JUNE, 14, 2, 2));
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
