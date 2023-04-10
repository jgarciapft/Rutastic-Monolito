package routefilter;

/**
 * Route filter that operates on SQL based data sources. The internal representation of the filter will be a string
 * with the equivalent SQL query
 *
 * @see RouteFilter
 */
public class SQLRouteFilter implements RouteFilter<String> {

    String query; // Stores the equivalent SQL query

    /**
     * Get a SQL route filter based of the specified sql query
     *
     * @param query SQL query that represents the filter
     */
    public SQLRouteFilter(String query) {
        this.query = query;
    }

    /**
     * @return The equivalent SQL query
     */
    @Override
    public String consume() {
        return query;
    }

    /**
     * Update the SQL query representing this route filter
     *
     * @param newRepresentation New SQL query
     */
    @Override
    public void updateUnderlyingRepresentation(String newRepresentation) {
        query = newRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return query != null && !query.isEmpty();
    }
}
