package helper.model;

import model.RouteToCategoriesMapping;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RouteToCategoryMappingMapper implements ModelMapper<RouteToCategoriesMapping> {

    /**
     * Parse a model instance of type {@code RouteToCategoriesMapping} from query parameters (or any other strings collection)
     *
     * @param queryParams query parameters (or any other strings collection)
     * @return The parsed model instance or null if any error occurred
     */
    @Override
    public RouteToCategoriesMapping parseFromQueryParams(Map<String, String[]> queryParams) {
        throw new NotImplementedException();
    }

    /**
     * Parse a model instance of type {@code RouteToCategoriesMapping} from a result set from a database query
     *
     * @param rs Result set containing queried columns from a database
     * @return The parsed instance or null if it any error occurred
     */
    @Override
    public RouteToCategoriesMapping parseFromResultSet(ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) return null;
            if (rs.isBeforeFirst()) rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        RouteToCategoriesMapping rtcm = new RouteToCategoriesMapping();

        try {
            rtcm.setRoute(rs.getLong("route"));
        } catch (SQLException ignored) {
        }
        try {
            rtcm.setCategory(rs.getLong("category"));
        } catch (SQLException ignored) {
        }

        return rtcm;
    }
}
