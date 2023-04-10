package dao;

import model.User;
import model.statistic.UserStatistic;

import java.util.List;

/**
 * Extended public interface for all DAO implementations for model class {@code User}. Specifies additional
 * methods all DAO implementations should support
 *
 * @see dao.implementations.DAOImplementation
 * @see User
 */
public interface UserDAO extends DAO<User> {

    /**
     * @param username Username of the user being retrieved
     * @return A user identified by its username or null if it couldn't be found
     */
    User getByUsername(String username);

    /**
     * @return A list of the users who are authors of the top monthly routes, ordered by descending number
     * of top monthly routes. For an user to be taken into account at least one of their routes need to have received
     * a kudo rating
     * @see UserStatistic
     */
    List<UserStatistic> getTopUsersByTopMonthlyRoutes();

    /**
     * @return A list of users ordered by descending average kudo rating of the routes they're authors of.
     * For an user to be taken into account at least one of their routes need to have received a kudo rating
     * @see UserStatistic
     */
    List<UserStatistic> getTopUsersByAvgKudos();

}
