package helper.model;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserModelMapper implements ModelMapper<User> {

    /**
     * Parse a model instance of type {@code User} from query parameters (or any other strings collection)
     *
     * @param queryParams query parameters (or any other strings collection)
     * @return The parsed model instance or null if any error occurred
     */
    @Override
    public User parseFromQueryParams(Map<String, String[]> queryParams) {
        User user = null;

        // Check that the map isn't null or empty

        if (queryParams != null && !queryParams.isEmpty()) {
            user = new User();

            if (queryParams.containsKey("id"))
                user.setId(Long.parseLong(queryParams.get("id")[0]));
            if (queryParams.containsKey("email"))
                user.setEmail(queryParams.get("email")[0].trim());
            if (queryParams.containsKey("usuario"))
                user.setUsername(queryParams.get("usuario")[0].trim());
            if (queryParams.containsKey("password"))
                user.setPassword(queryParams.get("password")[0].trim());
        }

        return user;
    }

    /**
     * Parse a model instance of type {@code User} from a result set from a database query
     *
     * @param rs Result set containing queried columns from a database
     * @return The parsed instance or null if it any error occurred
     */
    @Override
    public User parseFromResultSet(ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) return null;
            if (rs.isBeforeFirst()) rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        User user = new User();

        try {
            user.setId(rs.getLong("id"));
        } catch (SQLException ignored) {
        }
        try {
            user.setUsername(rs.getString("username"));
        } catch (SQLException ignored) {
        }
        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException ignored) {
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException ignored) {
        }
        try {
            user.setRole(rs.getString("role"));
        } catch (SQLException ignored) {
        }

        return user;
    }
}
