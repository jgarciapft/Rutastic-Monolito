package helper.model;

import model.RouteCategory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RouteCategoryModelMapper implements ModelMapper<RouteCategory> {

    /**
     * Parse a model instance of type {@code RouteCategory} from query parameters (or any other strings collection)
     *
     * @param queryParams query parameters (or any other strings collection)
     * @return The parsed model instance or null if any error occurred
     */
    @Override
    public RouteCategory parseFromQueryParams(Map<String, String[]> queryParams) {
        throw new NotImplementedException();
    }

    /**
     * Parse a model instance of type {@code RouteCategory} from a result set from a database query
     *
     * @param rs Result set containing queried columns from a database
     * @return The parsed instance or null if it any error occurred
     */
    @Override
    public RouteCategory parseFromResultSet(ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) return null;
            if (rs.isBeforeFirst()) rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RouteCategory routeCategory = new RouteCategory();

        try {
            routeCategory.setId(rs.getLong("id"));
        } catch (SQLException ignored) {
        }
        try {
            routeCategory.setName(rs.getString("name"));
        } catch (SQLException ignored) {
        }
        try {
            routeCategory.setDescription(rs.getString("description"));
        } catch (SQLException ignored) {
        }

        return routeCategory;
    }
}
