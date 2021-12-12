package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo extends BaseTo implements Serializable {
    private static final String DATE_TIME_FORMAT_PATTERN_FOR_UI = "yyyy-MM-dd HH:mm";
    private static final String EMPTY_FIELD_ERROR_MESSAGE = "must not be empty";

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = EMPTY_FIELD_ERROR_MESSAGE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT_PATTERN_FOR_UI)
    private final LocalDateTime dateTime;

    @Size(min = 2, max = 120)
    private final String description;

    @NotNull(message = EMPTY_FIELD_ERROR_MESSAGE)
    @Range(min = 10, max = 5000)
    private final Integer calories;

    @Nullable
    private final boolean excess;

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, Integer calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
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

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return excess == mealTo.excess &&
                Objects.equals(id, mealTo.id) &&
                Objects.equals(calories, mealTo.calories) &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
