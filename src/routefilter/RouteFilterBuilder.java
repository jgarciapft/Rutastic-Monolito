package routefilter;

/**
 * Builder of route filter. To obtain the built filter call {@link #buildFilter}
 *
 * @param <T> The route filter this builder will build
 * @see RouteFilter
 */
public interface RouteFilterBuilder<T extends RouteFilter<?>> {

    /**
     * @return The built route filter
     */
    T buildFilter();


    /**
     * Reset the contents of this builder. A call to this method leaves the builder at its initial state to start
     * designing a new filter from scratch
     */
    void clear();

}
