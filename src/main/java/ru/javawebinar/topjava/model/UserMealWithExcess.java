package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    private final CaloriesPerDayAccumulator accumulator;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        accumulator = null;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, CaloriesPerDayAccumulator accumulator) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        excess = false;
        this.accumulator = accumulator;
    }

    public boolean isExcess() {
        return accumulator == null ? excess : accumulator.caloriesPerDayValue > accumulator.caloriesPerDayLimit;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + isExcess() +
                '}';
    }

    public static class CaloriesPerDayAccumulator {
        private final int caloriesPerDayLimit;

        private int caloriesPerDayValue;

        public CaloriesPerDayAccumulator(int caloriesPerDayLimit) {
            this.caloriesPerDayLimit = caloriesPerDayLimit;
        }

        public void addCalories(int x) {
            caloriesPerDayValue += x;
        }
    }
}
