package dao;

import model.RouteToCategoriesMapping;

import java.util.List;

/**
 * Extended public interface for all DAO implementations for model class {@code RouteToCategoriesMapping}.
 * Specifies additional methods all DAO implementations should support
 *
 * @see dao.implementations.DAOImplementation
 * @see RouteToCategoriesMapping
 */
public interface RouteToCategoriesMappingDAO extends DAO<RouteToCategoriesMapping> {

    /**
     * @param routeId Route identifier
     * @return All categories of a given route
     */
    List<RouteToCategoriesMapping> getAllByRoute(long routeId);

    /**
     * @param categoryId Category to use as a filter identified by its id
     * @return All routes that are categorized by a specific category
     */
    List<RouteToCategoriesMapping> getAllByCategory(long categoryId);

    /**
     * @param categoryName Category to use as a filter identified by its name
     * @return All routes that are categorized by a specific category
     */
    List<RouteToCategoriesMapping> getAllByCategory(String categoryName);

    /**
     * Add multiple categories to a specific route. In the event that an error occurs, the operation is canceled
     *
     * @param bulk     Categories mappings specifying multiple categories to add to a single route
     * @param isAtomic A true value indicates that the operation should be performed atomically
     * @return Whether all the categories could be assigned to the route
     */
    boolean addInBulk(List<RouteToCategoriesMapping> bulk, boolean isAtomic);

    /**
     * Delete multiple categories from a route. In the event that an error occurs, the operation is canceled
     *
     * @param bulk     Categories mappings specifying multiple categories to be deleted from a single route
     * @param isAtomic A true value indicates that the operation should be performed atomically
     * @return Whether all the categories could be deleted from the route
     */
    boolean deleteInBulk(List<RouteToCategoriesMapping> bulk, boolean isAtomic);

}
