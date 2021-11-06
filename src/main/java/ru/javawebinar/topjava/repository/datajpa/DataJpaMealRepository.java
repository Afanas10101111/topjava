package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() || get(meal.id(), userId) != null) {
            meal.setUser(userRepository.getById(userId));
            return crudRepository.save(meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
        //return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meal get(int id, int userId) {
        return crudRepository.findUserMeal(id, userId);
        //return crudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllUserMeal(userId);
        //return crudRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllUserMealBetween(userId, startDateTime, endDateTime);
    }

    @Override
    public Meal getMealWithUser(int id, int userId) {
        return crudRepository.findMealAndUser(id, userId);
    }
}
