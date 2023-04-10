package helper.model;

import helper.DateTimeUtils;
import model.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class RouteModelMapper implements ModelMapper<Route> {

    private static final String DB_CATEGORY_SEPARATOR = ",";

    /**
     * Parse a model instance of type {@code Route} from query parameters (or any other strings collection)
     *
     * @param queryParams query parameters (or any other strings collection)
     * @return The parsed model instance or null if any error occurred
     */
    @Override
    public Route parseFromQueryParams(Map<String, String[]> queryParams) {
        Route route = null;

        if (queryParams != null && !queryParams.isEmpty()) {
            route = new Route();

            if (queryParams.containsKey("id"))
                route.setId(Long.parseLong(queryParams.get("id")[0]));
            if (queryParams.containsKey("titulo"))
                route.setTitle(queryParams.get("titulo")[0].trim());
            if (queryParams.containsKey("descripcion"))
                route.setDescription(queryParams.get("descripcion")[0].trim());
            if (queryParams.containsKey("categorias"))
                route.setCategories(parseCategoriesString(queryParams.get("categorias")));
            if (queryParams.containsKey("distancia"))
                route.setDistance(Integer.parseInt(queryParams.get("distancia")[0].trim()));
            if (queryParams.containsKey("elevacion"))
                route.setElevation(Integer.parseInt(queryParams.get("elevacion")[0].trim()));
            if (queryParams.containsKey("duracion"))
                route.setDuration(Integer.parseInt(queryParams.get("duracion")[0].trim()));
            if (queryParams.containsKey("dificultad"))
                route.setSkillLevel(queryParams.get("dificultad")[0].trim());
        }

        return route;
    }

    /**
     * Parse a model instance of type {@code Route} from a result set from a database query
     *
     * @param rs Result set containing queried columns from a database
     * @return The parsed instance or null if it any error occurred
     */
    @Override
    public Route parseFromResultSet(ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) return null;
            if (rs.isBeforeFirst()) rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Route route = new Route();

        // Try parsing route attributes, except route categories

        try {
            route.setId(rs.getLong("id"));
        } catch (SQLException ignored) {
        }
        try {
            route.setCreatedByUser(rs.getLong("created_by_user"));
        } catch (SQLException ignored) {
        }
        try {
            route.setTitle(rs.getString("title"));
        } catch (SQLException ignored) {
        }
        try {
            route.setDescription(rs.getString("description"));
        } catch (SQLException ignored) {
        }
        try {
            route.setDistance(rs.getInt("distance"));
        } catch (SQLException ignored) {
        }
        try {
            route.setDuration(rs.getInt("duration"));
        } catch (SQLException ignored) {
        }
        try {
            route.setElevation(rs.getInt("elevation"));
        } catch (SQLException ignored) {
        }
        try {
            route.setCreationDate(DateTimeUtils
                    .formatEpochTime(rs.getLong("creation_date"), DateTimeUtils.TimeResolution.SECONDS));
        } catch (SQLException ignored) {
        }
        try {
            route.setSkillLevel(rs.getString("skill_level"));
        } catch (SQLException ignored) {
        }
        try {
            route.setKudos(rs.getInt("kudos"));
        } catch (SQLException ignored) {
        }
        try {
            route.setBlocked(rs.getInt("blocked") == 1);
        } catch (SQLException ignored) {
        }
        try {
            // Isolate each category name and rejoin the categories string with the separator specified in Route model
            route.setCategories(String.join(Route.CATEGORY_SEPARATOR,
                    rs.getString("categories").split(DB_CATEGORY_SEPARATOR)));
        } catch (SQLException ignored) {
        }

        return route;
    }

    /**
     * Parse categories string from string array
     *
     * @param categories String array containing category names
     * @return Parsed categories string
     */
    private String parseCategoriesString(String[] categories) {
        Iterator<String> it = Arrays.stream(categories).iterator();
        StringBuilder stringBuilder = new StringBuilder();

        do {
            stringBuilder.append(it.next().trim());
            if (it.hasNext())
                stringBuilder.append(Route.CATEGORY_SEPARATOR);
        } while (it.hasNext());

        return stringBuilder.toString();
    }
}
