package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealReceiveTo extends BaseTo {
    private static final String DATE_TIME_FORMAT_PATTERN_FOR_UI = "yyyy-MM-dd HH:mm";
    private static final String EMPTY_FIELD_ERROR_MESSAGE = "must not be empty";

    @NotNull(message = EMPTY_FIELD_ERROR_MESSAGE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT_PATTERN_FOR_UI)
    private final LocalDateTime dateTime;

    @NotBlank(message = EMPTY_FIELD_ERROR_MESSAGE)
    @Size(min = 2, max = 120)
    private final String description;

    @NotNull(message = EMPTY_FIELD_ERROR_MESSAGE)
    @Range(min = 10, max = 5000)
    private final Integer calories;

    @ConstructorProperties({"id", "dateTime", "description", "calories"})
    public MealReceiveTo(Integer id, LocalDateTime dateTime, String description, Integer calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealReceiveTo mealTo = (MealReceiveTo) o;
        return Objects.equals(id, mealTo.id) &&
                Objects.equals(calories, mealTo.calories) &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories);
    }

    @Override
    public String toString() {
        return "MealReceiveTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
