package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("meals")
public class JspMealController extends AbstractMealController {
    public static final String MEAL_ATTR = "meal";
    public static final String MEALS_ATTR = "meals";

    public static final String MEALS_LINK = "meals";
    public static final String REDIRECT_MEALS = "redirect:/meals";
    public static final String MEAL_FORM_LINK = "mealForm";

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute(MEALS_ATTR, getAll());
        return MEALS_LINK;
    }

    @GetMapping("delete")
    public String delete(Integer id) {
        super.delete(id);
        return REDIRECT_MEALS;
    }

    @GetMapping("update")
    public String update(
            Model model,
            Integer id
    ) {
        model.addAttribute(MEAL_ATTR, get(id));
        return MEAL_FORM_LINK;
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute(MEAL_ATTR, new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return MEAL_FORM_LINK;
    }

    @GetMapping("filter")
    public String filter(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime endTime
    ) {
        model.addAttribute(MEALS_ATTR, getBetween(startDate, startTime, endDate, endTime));
        return MEALS_LINK;
    }

    @PostMapping
    public String save(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            String description,
            int calories,
            @RequestParam(required = false) Integer id
    ) {
        Meal meal = new Meal(dateTime, description, calories);
        if (id != null) {
            update(meal, id);
        } else {
            create(meal);
        }
        return REDIRECT_MEALS;
    }
}
