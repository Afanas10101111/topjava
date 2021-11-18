package ru.javawebinar.topjava.web.meal;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {
    public static final String MEAL_ATTR = "meal";
    public static final String MEALS_ATTR = "meals";

    public static final String MEALS_LINK = "/meals";
    public static final String REDIRECT_MEALS = "redirect:/meals";
    public static final String MEAL_FORM_LINK = "/mealForm";

    public JspMealController(MealService service) {
        super(service, LoggerFactory.getLogger(JspMealController.class));
    }

    @GetMapping(MEALS_LINK)
    public String getAll(Model model) {
        model.addAttribute(MEALS_ATTR, getAll());
        return MEALS_LINK;
    }

    @GetMapping(MEALS_LINK + "/delete")
    public String delete(HttpServletRequest request) {
        delete(getId(request));
        return REDIRECT_MEALS;
    }

    @GetMapping(MEALS_LINK + "/update")
    public String update(HttpServletRequest request) {
        request.setAttribute(MEAL_ATTR, get(getId(request)));
        return MEAL_FORM_LINK;
    }

    @GetMapping(MEALS_LINK + "/create")
    public String create(Model model) {
        model.addAttribute(MEAL_ATTR, new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return MEAL_FORM_LINK;
    }

    @GetMapping(MEALS_LINK + "/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute(MEALS_ATTR, getBetween(startDate, startTime, endDate, endTime));
        return MEALS_LINK;
    }

    @PostMapping(MEALS_LINK)
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        if (StringUtils.hasLength(request.getParameter("id"))) {
            update(meal, getId(request));
        } else {
            create(meal);
        }
        return REDIRECT_MEALS;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
