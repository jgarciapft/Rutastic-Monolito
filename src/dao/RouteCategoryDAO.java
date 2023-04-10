package dao;

import model.RouteCategory;

/**
 * Extended public interface for all DAO implementations for model class {@code RouteCategory}. Specifies additional
 * methods all DAO implementations should support
 *
 * @see dao.implementations.DAOImplementation
 * @see RouteCategory
 */
public interface RouteCategoryDAO extends DAO<RouteCategory> {

    /**
     * @param name Route category name
     * @return The category identified by its name or null if it couldn't be found
     */
    RouteCategory getByName(String name);

}
