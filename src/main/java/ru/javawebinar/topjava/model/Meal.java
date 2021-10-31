package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date_time"}))
@NamedQuery(name = Meal.GET,
        query = "SELECT m FROM Meal m WHERE m.id=:" + Meal.MEAL_ID_PARAM_NAME + " AND m.user.id=:" + Meal.USER_ID_PARAM_NAME)
@NamedQuery(name = Meal.DELETE,
        query = "DELETE FROM Meal m WHERE m.id=:" + Meal.MEAL_ID_PARAM_NAME + " AND m.user.id=:" + Meal.USER_ID_PARAM_NAME)
@NamedQuery(name = Meal.ALL_SORTED,
        query = "SELECT m FROM Meal m WHERE m.user.id=:" + Meal.USER_ID_PARAM_NAME + " ORDER BY m.dateTime DESC")
@NamedQuery(name = Meal.FILTERED,
        query = "SELECT m FROM Meal m WHERE m.user.id=:" + Meal.USER_ID_PARAM_NAME +
                " AND m.dateTime>=:" + Meal.START_DATE_TIME_PARAM_NAME +
                " AND m.dateTime<:" + Meal.END_DATE_TIME_PARAM_NAME + " ORDER BY m.dateTime DESC")
public class Meal extends AbstractBaseEntity {
    public static final String GET = "Meal.get";
    public static final String DELETE = "Meal.delete";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String FILTERED = "Meal.filtered";

    public static final String MEAL_ID_PARAM_NAME = "id";
    public static final String USER_ID_PARAM_NAME = "userId";
    public static final String START_DATE_TIME_PARAM_NAME = "startDateTime";
    public static final String END_DATE_TIME_PARAM_NAME = "endDateTime";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 100)
    private String description;

    @Column(nullable = false)
    @Range(min = 1, max = 100000)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
