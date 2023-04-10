package routefilter;

/**
 * Route filter specification. A route filter is a mechanism to specify an inclusive filter or sorting behaviour for
 * routes retrieved from a DAO implementation, in other words, from a data source
 *
 * @param <T> Underlying type holding the filter final representation. The type that will be used by the data source
 *            to apply the filter
 * @see dao.DAO
 * @see dao.implementations.DAOImplementation
 */
public interface RouteFilter<T> {

    /**
     * Consume this filter. Get the final representation that will be used by a DAO implementation
     *
     * @return The final filter representation
     */
    T consume();

    /**
     * Update / set the internal route filter representation
     *
     * @param newRepresentation New route filter representation
     */
    void updateUnderlyingRepresentation(T newRepresentation);

    /**
     * @return If this filter is ready to be consumed
     */
    boolean isValid();

}
