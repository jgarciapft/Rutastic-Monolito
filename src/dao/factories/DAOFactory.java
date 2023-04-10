package dao.factories;

import dao.DAO;
import dao.implementations.DAODependencyConfigurator;
import dao.implementations.DAOImplementation;

/**
 * Factory of DAO objects compliant to a specific DAO implementation as a mean of grouping DAOs that access a concrete
 * data source together. It provides means to instantiate and configure each DAO via a {@link DAODependencyConfigurator}
 *
 * @param <T>
 * @see DAOImplementation
 * @see DAODependencyConfigurator
 */
public interface DAOFactory<T extends DAOImplementation> {

    /**
     * Request the DAO or DAO sub-interface for the model class specified by the generic type {@literal <U>}, compliant
     * to the DAO Implementation of this factory
     *
     * @param modelClass The class of the model being requested
     * @param <U>        Model being requested
     * @param <V>        DAO or DAO sub-interface for the model being requested
     * @return DAO or DAO sub-interface for the model being requested
     * @see DAO
     */
    <U, V extends DAO<U>> V forModel(Class<U> modelClass);

    /**
     * @return The dependency configurator used by this factory to configure the dependencies of the DAOs it can
     * generate
     * @see DAODependencyConfigurator
     */
    DAODependencyConfigurator<T> getDAODependencyConfigurator();

    /**
     * The dependency configurator used by this factory to configure the dependencies of the DAOs it can generate
     *
     * @param dependencyConfigurator The dependencies configurator
     * @see DAODependencyConfigurator
     */
    void setDAODependencyConfigurator(DAODependencyConfigurator<T> dependencyConfigurator);

    /**
     * @return The DAO implementation this factory supports
     * @see DAOImplementation
     */
    Class<T> getDAOImplementation();

    /**
     * A call to this method will cause all DAO objects to be configured using the set dependency configurator.
     * If there's no dependencies configurator set the call is a no-op
     *
     * @param additionalDependencies Any additional objects needed to configure DAO dependencies
     */
    void configureAllDAODependencies(Object... additionalDependencies);

}
