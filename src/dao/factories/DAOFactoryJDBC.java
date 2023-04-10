package dao.factories;

import dao.*;
import dao.implementations.DAODependencyConfigurator;
import dao.implementations.DAOImplJDBC;
import model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DAOFactory for JDBC DAO implementations. It stores one instance of each JDBC DAO and returns it when requested
 *
 * @see DAOFactory
 * @see DAOImplJDBC
 */
public class DAOFactoryJDBC implements DAOFactory<DAOImplJDBC> {

    private final Map<Class<?>, DAOImplJDBC> jdbcDAOCollection;
    private DAODependencyConfigurator<DAOImplJDBC> dependencyConfigurator;

    public DAOFactoryJDBC() {

        // Instantiate and store each uninitialized DAO instance

        Map<Class<?>, DAOImplJDBC> modifiableFactory = new HashMap<>();
        JDBCUserDAO jdbcUserDAO = new JDBCUserDAO();
        JDBCRouteDAO jdbcRouteDAO = new JDBCRouteDAO();
        JDBCRouteCategoryDAO jdbcRouteCategoryDAO = new JDBCRouteCategoryDAO();
        JDBCKudoEntryDAO jdbcKudoEntryDAO = new JDBCKudoEntryDAO();
        JDBCRouteToCategoriesMappingDAO jdbcRouteToCategoriesMappingDAO = new JDBCRouteToCategoriesMappingDAO();

        modifiableFactory.put(User.class, jdbcUserDAO);
        modifiableFactory.put(Route.class, jdbcRouteDAO);
        modifiableFactory.put(RouteCategory.class, jdbcRouteCategoryDAO);
        modifiableFactory.put(KudoEntry.class, jdbcKudoEntryDAO);
        modifiableFactory.put(RouteToCategoriesMapping.class, jdbcRouteToCategoriesMappingDAO);

        jdbcDAOCollection = Collections.unmodifiableMap(modifiableFactory);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U, V extends DAO<U>> V forModel(Class<U> modelClass) {
        DAOImplJDBC dao = jdbcDAOCollection.getOrDefault(modelClass, null);

        // Return the requested DAO casted to the DAO sub-interface that was requested, or null if not found

        if (dao != null)
            return (V) dao;
        else
            return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureAllDAODependencies(Object... additionalDependencies) {
        if (dependencyConfigurator != null) {
            for (DAOImplJDBC dao : jdbcDAOCollection.values())
                dependencyConfigurator.dependenciesConfigurationStrategy(dao, additionalDependencies);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DAODependencyConfigurator<DAOImplJDBC> getDAODependencyConfigurator() {
        return dependencyConfigurator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDAODependencyConfigurator(DAODependencyConfigurator<DAOImplJDBC> dependencyConfigurator) {
        this.dependencyConfigurator = dependencyConfigurator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<DAOImplJDBC> getDAOImplementation() {
        return DAOImplJDBC.class;
    }
}
