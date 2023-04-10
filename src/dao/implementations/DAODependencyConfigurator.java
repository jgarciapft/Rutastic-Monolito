package dao.implementations;

/**
 * A functional interface for specifying the dependencies configuration process for a DAO using a specific
 * DAO implementation. It enables the use of lambda expressions.
 *
 * @param <T> The DAO implementation used to operate a data source
 * @see DAOImplementation
 */
@FunctionalInterface
public interface DAODependencyConfigurator<T extends DAOImplementation> {

    /**
     * Specify how to configure the dependencies for a DAO
     *
     * @param dao          DAO object whose dependencies are being configured
     * @param dependencies Optional objects provided to configure the required dependencies of the DAO object
     */
    void dependenciesConfigurationStrategy(T dao, Object... dependencies);

}
