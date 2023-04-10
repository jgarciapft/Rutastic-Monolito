package helper.model;

import java.sql.ResultSet;
import java.util.Map;

/**
 * A Model Mapper is an utility class that can parse a model object from another data representation structure or
 * language. This interface contains the representations that should be supported by all model mappers, specifically
 * query parameters (or any other strings collection) and a result set from a database query.
 * <p></p>
 * It assumes no guarantees about the completeness of the data being passed as parsing material, that is, it doesn't
 * assume that all model attributes are susceptible of being parsed.
 *
 * @param <T> The model class that this model mapper can parse
 */
public interface ModelMapper<T> {

    /**
     * Parse a model instance of type {@literal <T>} from query parameters (or any other strings collection)
     *
     * @param queryParams query parameters (or any other strings collection)
     * @return The parsed model instance or null if any error occurred
     */
    T parseFromQueryParams(Map<String, String[]> queryParams);

    /**
     * Parse a model instance of type {@literal <T>} from a result set from a database query
     *
     * @param rs Result set containing queried columns from a database
     * @return The parsed instance or null if it any error occurred
     */
    T parseFromResultSet(ResultSet rs);

}
