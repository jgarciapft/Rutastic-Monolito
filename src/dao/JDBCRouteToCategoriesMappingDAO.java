package dao;

import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import helper.model.ModelMapper;
import helper.model.ModelMapperFactory;
import model.RouteCategory;
import model.RouteToCategoriesMapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * JDBC Implementation for the {@code RouteToCategoriesMapping} DAO
 *
 * @see DAOImplJDBC
 * @see RouteToCategoriesMappingDAO
 * @see RouteToCategoriesMapping
 */
public class JDBCRouteToCategoriesMappingDAO implements RouteToCategoriesMappingDAO, DAOImplJDBC {

    private static final Logger logger = Logger.getLogger(JDBCRouteToCategoriesMappingDAO.class.getName());
    private boolean dependenciesConfigured;
    private Connection connection;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RouteToCategoriesMapping> getAll() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL ROUTE CATEGORY MAPPINGS");

        RouteToCategoriesMapping currentMapping;
        List<RouteToCategoriesMapping> allMappings = new ArrayList<>();
        ModelMapper<RouteToCategoriesMapping> rcMappingModelMapper =
                ModelMapperFactory.get().forModel(RouteToCategoriesMapping.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routetocategoriesmapping");

            while (rs.next()) {
                currentMapping = rcMappingModelMapper.parseFromResultSet(rs);
                if (currentMapping != null) {
                    allMappings.add(currentMapping);
                    logger.info(String.format("[FETCHED Route Category Mapping] route: %d | category: %d",
                            currentMapping.getRoute(),
                            currentMapping.getCategory()));
                } else {
                    logger.warning("Attempted to read a NULL Route Category Mapping");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allMappings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RouteToCategoriesMapping> getAllByRoute(long routeId) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL ROUTE CATEGORY MAPPINGS FOR ROUTEID (" + routeId + ")");

        RouteToCategoriesMapping currentMapping;
        List<RouteToCategoriesMapping> allMappings = new ArrayList<>();
        ModelMapper<RouteToCategoriesMapping> rcMappingModelMapper =
                ModelMapperFactory.get().forModel(RouteToCategoriesMapping.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routetocategoriesmapping WHERE route = " + routeId);

            while (rs.next()) {
                currentMapping = rcMappingModelMapper.parseFromResultSet(rs);
                if (currentMapping != null) {
                    allMappings.add(currentMapping);
                    logger.info(String.format("[FETCHED Route Category Mapping] route: %d | category: %d",
                            currentMapping.getRoute(),
                            currentMapping.getCategory()));
                } else {
                    logger.warning("Attempted to read a NULL Route Category Mapping");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allMappings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RouteToCategoriesMapping> getAllByCategory(long categoryId) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL ROUTE CATEGORY MAPPINGS FOR CATEGORYID (" + categoryId + ")");

        RouteToCategoriesMapping currentMapping;
        List<RouteToCategoriesMapping> allMappings = new ArrayList<>();
        ModelMapper<RouteToCategoriesMapping> rcMappingModelMapper =
                ModelMapperFactory.get().forModel(RouteToCategoriesMapping.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routetocategoriesmapping WHERE category = " + categoryId);

            while (rs.next()) {
                currentMapping = rcMappingModelMapper.parseFromResultSet(rs);
                if (currentMapping != null) {
                    allMappings.add(currentMapping);
                    logger.info(String.format("[FETCHED Route Category Mapping] route: %d | category: %d",
                            currentMapping.getRoute(),
                            currentMapping.getCategory()));
                } else {
                    logger.warning("Attempted to read a NULL Route Category Mapping");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allMappings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RouteToCategoriesMapping> getAllByCategory(String categoryName) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL ROUTE CATEGORY MAPPINGS FOR CATEGORY NAME (" + categoryName + ")");

        RouteCategoryDAO routeCategoryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(RouteCategory.class);
        RouteCategory category = routeCategoryDAO.getByName(categoryName);

        return getAllByCategory(category.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public RouteToCategoriesMapping getById(long... id) {
        if (id.length != 2) throw new IllegalArgumentException("Wrong number of identifiers. Expected 2");

        if (!dependenciesConfigured()) return null;

        RouteToCategoriesMapping rcMapping = null;
        ModelMapper<RouteToCategoriesMapping> rcMappingModelMapper =
                ModelMapperFactory.get().forModel(RouteToCategoriesMapping.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM routetocategoriesmapping " +
                    "WHERE route = %d AND category = %d", id[0], id[1]));

            if (rs.next()) {
                rcMapping = rcMappingModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED Route Category Mapping] route: %d | category: %d",
                        rcMapping.getRoute(),
                        rcMapping.getCategory()));
            } else {
                logger.warning("There's no Route Category Mapping by the id (" + id[0] + ", " + id[1] + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return rcMapping;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(RouteToCategoriesMapping instance) {
        return add(instance, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(RouteToCategoriesMapping instance, boolean isAtomic) {
        long SQLERROR = -1L;
        long[] idOfAddedInstance = {instance.getRoute(), instance.getCategory()}; // Copy provided id to return on success

        if (!dependenciesConfigured()) return new long[]{SQLERROR};

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO routetocategoriesmapping(route, category) VALUES (%d, %d)",
                    instance.getRoute(),
                    instance.getCategory()));

            if (isAtomic) connection.commit();
            st.close();

            logger.info(String.format("[NEW ROUTE CATEGORY MAPPING CREATED] route: %d | category: %d",
                    instance.getRoute(),
                    instance.getCategory()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                idOfAddedInstance = new long[]{SQLERROR}; // Set error status
            }
        }

        return idOfAddedInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addInBulk(List<RouteToCategoriesMapping> bulk, boolean isAtomic) {
        boolean success = true;
        long SQLERROR = -1L;

        logger.info("[BEGINNING OF ROUTE CATEGORY BULK STORING]");

        for (int i = 0; i < bulk.size() && success; i++) {
            success = add(bulk.get(i), isAtomic)[0] != SQLERROR;

            if (!success)
                logger.warning("[ERROR IN BULK STORING ROUTE CATEGORY MAPPING WITH ID (" +
                        bulk.get(i).getRoute() + ", " + bulk.get(i).getCategory() + ")");
        }

        logger.info("[END OF ROUTE CATEGORY BULK STORING]");

        if (isAtomic) {
            try {
                if (success) {
                    connection.commit();
                } else {
                    logger.warning("[ERROR IN ROUTE CATEGORY MAPPING BULK STORING]");
                    connection.rollback();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return success;
    }

    /**
     * Updates on route to categories mappings are dangerous due to the risk of ending with duplicate mappings, so
     * this operation is not supported.
     *
     * @throws UnsupportedOperationException This operation is not supported
     */
    @Override
    public boolean save(RouteToCategoriesMapping instance) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates on route to categories mappings are dangerous due to the risk of ending with duplicate mappings, so
     * this operation is not supported.
     *
     * @throws UnsupportedOperationException This operation is not supported
     */
    @Override
    public boolean save(RouteToCategoriesMapping instance, boolean isAtomic) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public boolean deleteById(long... id) {
        return deleteById(true, id);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public boolean deleteById(boolean isAtomic, long... id) {
        if (id.length != 2) throw new IllegalArgumentException("Wrong number of identifiers. Expected 2");

        if (!dependenciesConfigured()) return false;

        boolean deletionSuccessful = false;

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("DELETE FROM routetocategoriesmapping WHERE route = %d AND category = %d",
                    id[0], id[1]));

            if (isAtomic) connection.commit();
            deletionSuccessful = true;
            st.close();

            logger.info("[Route Category Mapping with the id (" + id[0] + ", " + id[1] + ") has been deleted]");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return deletionSuccessful;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteInBulk(List<RouteToCategoriesMapping> bulk, boolean isAtomic) {
        boolean success = true;

        logger.info("[BEGINNING OF ROUTE CATEGORY BULK DELETION]");

        for (int i = 0; i < bulk.size() && success; i++) {
            success = deleteById(isAtomic, bulk.get(i).getRoute(), bulk.get(i).getCategory());

            if (!success)
                logger.warning("[ERROR IN BULK DELETION ROUTE CATEGORY MAPPING WITH ID (" +
                        bulk.get(i).getRoute() + ", " + bulk.get(i).getCategory() + ")");
        }

        logger.info("[END OF ROUTE CATEGORY BULK DELETION]");

        if (isAtomic) {
            try {
                if (success) {
                    connection.commit();
                } else {
                    logger.warning("[ERROR IN ROUTE CATEGORY MAPPING BULK DELELTION]");
                    connection.rollback();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dependenciesConfigured() {
        return dependenciesConfigured;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDependenciesConfigured(boolean status) {
        dependenciesConfigured = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
