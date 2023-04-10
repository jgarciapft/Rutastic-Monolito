package dao.implementations;

import model.Route;
import routefilter.SQLRouteFilter;

import java.util.List;

/**
 * Extension of the JDBC operations possible for Route model instances
 */
public interface RouteDAOImplJDBC extends DAOImplJDBC {

    /**
     * Execute the specified route filter to retrieve a filtered route collection
     *
     * @param sqlRouteFilter The SQL route filter
     * @return A list with only the route instances that satisfy the route filter
     */
    List<Route> executeFilter(SQLRouteFilter sqlRouteFilter);

}
