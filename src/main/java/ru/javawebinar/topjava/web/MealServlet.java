package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String MEALS_JSP = "meals.jsp";
    private static final String UPDATE_JSP = "update.jsp";
    private static final String MEALS_URI = "meals";
    private static final int CALORIES_DAY_LIMIT = 2000;

    private Logger log;
    private MealRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        log = getLogger(MealServlet.class);
        repository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String forwardTo = MEALS_JSP;
        String action = Optional.ofNullable(request.getParameter("action")).orElse("");
        log.debug("doGet -> action = {}", action);

        switch (action.toLowerCase()) {
            case "add":
                forwardTo = UPDATE_JSP;
                break;
            case "update":
                forwardTo = UPDATE_JSP;
                Meal meal = repository.getById(getIdFromRequest(request));
                request.setAttribute("meal", meal);
                break;
            case "delete":
                repository.delete(getIdFromRequest(request));
                response.sendRedirect(MEALS_URI);
                return;
            default:
                List<MealTo> mealsTo = MealsUtil.withoutFilter(repository.getAll(), CALORIES_DAY_LIMIT);
                log.debug("mealsTo size = {}", mealsTo.size());
                request.setAttribute("mealsTo", mealsTo);
        }
        request.getRequestDispatcher(forwardTo).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Long mealId = id.length() == 0 ? null : Long.parseLong(id);
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        log.debug("doPost -> mealId = {}; dateTime = {}; description = {}; calories = {}", mealId, dateTime, description, calories);

        Meal meal = new Meal(mealId, dateTime, description, calories);
        if (mealId == null) {
            repository.add(meal);
        } else {
            repository.update(meal);
        }
        response.sendRedirect(MEALS_URI);
    }

    private Long getIdFromRequest(HttpServletRequest request) {
        return Long.parseLong(request.getParameter("id"));
    }
}
