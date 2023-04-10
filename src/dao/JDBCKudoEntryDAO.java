package dao;

import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import helper.DateTimeUtils;
import helper.model.ModelMapper;
import helper.model.ModelMapperFactory;
import model.KudoEntry;
import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * JDBC Implementation for the {@code KudoEntry} DAO
 *
 * @see KudoEntry
 * @see KudoEntryDAO
 * @see DAOImplJDBC
 */
public class JDBCKudoEntryDAO implements KudoEntryDAO, DAOImplJDBC {

    private static final Logger logger = Logger.getLogger(JDBCKudoEntryDAO.class.getName());
    private boolean dependenciesConfigured;
    private Connection connection;

    public JDBCKudoEntryDAO() {
        dependenciesConfigured = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KudoEntry> getAll() {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL KUDO ENTRIES");

        KudoEntry currentEntry;
        List<KudoEntry> allEntries = new ArrayList<>();
        ModelMapper<KudoEntry> kEntryModelMapper = ModelMapperFactory.get().forModel(KudoEntry.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routekudosregistry");

            while (rs.next()) {
                currentEntry = kEntryModelMapper.parseFromResultSet(rs);
                if (currentEntry != null) {
                    allEntries.add(currentEntry);
                    logger.info(String.format("[FETCHED Kudo Entry] user: %d | route: %s | modifier: %d | submission date: %s",
                            currentEntry.getUser(),
                            currentEntry.getRoute(),
                            currentEntry.getModifier(),
                            DateTimeUtils.formatEpochTime(DateTimeUtils.TimeResolution.SECONDS,
                                    (int) currentEntry.getSubmissionDate())));
                } else {
                    logger.warning("Attempted to read a NULL kudo entry");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KudoEntry> getAllByUser(long userId) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL KUDO ENTRIES FOR USERID (" + userId + ")");

        KudoEntry currentEntry;
        List<KudoEntry> allEntries = new ArrayList<>();
        ModelMapper<KudoEntry> kEntryModelMapper = ModelMapperFactory.get().forModel(KudoEntry.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routekudosregistry WHERE user = " + userId);

            while (rs.next()) {
                currentEntry = kEntryModelMapper.parseFromResultSet(rs);
                if (currentEntry != null) {
                    allEntries.add(currentEntry);
                    logger.info(String.format("[FETCHED Kudo Entry] user: %d | route: %s | modifier: %d | submission date: %s",
                            currentEntry.getUser(),
                            currentEntry.getRoute(),
                            currentEntry.getModifier(),
                            DateTimeUtils.formatEpochTime(DateTimeUtils.TimeResolution.SECONDS,
                                    (int) currentEntry.getSubmissionDate())));
                } else {
                    logger.warning("Attempted to read a NULL kudo entry");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KudoEntry> getAllByUser(String username) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL KUDO ENTRIES FOR USERNAME (" + username + ")");

        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);
        User user = userDAO.getByUsername(username);

        return getAllByUser(user.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KudoEntry> getAllByRoute(long routeId) {
        if (!dependenciesConfigured()) return null;

        logger.info("FETCHING ALL KUDO ENTRIES FOR ROUTEID (" + routeId + ")");

        KudoEntry currentEntry;
        List<KudoEntry> allEntries = new ArrayList<>();
        ModelMapper<KudoEntry> kEntryModelMapper = ModelMapperFactory.get().forModel(KudoEntry.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM routekudosregistry WHERE route = " + routeId);

            while (rs.next()) {
                currentEntry = kEntryModelMapper.parseFromResultSet(rs);
                if (currentEntry != null) {
                    allEntries.add(currentEntry);
                    logger.info(String.format("[FETCHED Kudo Entry] user: %d | route: %s | modifier: %d | submission date: %s",
                            currentEntry.getUser(),
                            currentEntry.getRoute(),
                            currentEntry.getModifier(),
                            DateTimeUtils.formatEpochTime(DateTimeUtils.TimeResolution.SECONDS,
                                    (int) currentEntry.getSubmissionDate())));
                } else {
                    logger.warning("Attempted to read a NULL kudo entry");
                }
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allEntries;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException On call with wrong number of identifiers
     */
    @Override
    public KudoEntry getById(long... id) {
        if (id.length != 2) throw new IllegalArgumentException("Wrong number of identifiers. Expected 2");

        if (!dependenciesConfigured()) return null;

        KudoEntry kudoEntry = null;
        ModelMapper<KudoEntry> kEntryModelMapper = ModelMapperFactory.get().forModel(KudoEntry.class);

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM routekudosregistry " +
                    "WHERE user = %d AND route = %d", id[0], id[1]));

            if (rs.next()) {
                kudoEntry = kEntryModelMapper.parseFromResultSet(rs);
                logger.info(String.format("[FETCHED Kudo Entry] user: %d | route: %s | modifier: %d | submission date: %s",
                        kudoEntry.getUser(),
                        kudoEntry.getRoute(),
                        kudoEntry.getModifier(),
                        DateTimeUtils.formatEpochTime(DateTimeUtils.TimeResolution.SECONDS,
                                (int) kudoEntry.getSubmissionDate())));
            } else {
                logger.warning("There's no Kudo entry by the id (" + id[0] + ")");
            }

            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return kudoEntry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(KudoEntry instance) {
        return add(instance, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long[] add(KudoEntry instance, boolean isAtomic) {
        long SQLERROR = -1L;
        long[] idOfAddedInstance = {instance.getUser(), instance.getRoute()}; // Copy provided id to return on success

        if (!dependenciesConfigured()) return new long[]{SQLERROR};

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO routekudosregistry(user, route, modifier) VALUES (%d, %d, %d)",
                    instance.getUser(),
                    instance.getRoute(),
                    instance.getModifier()));

            if (isAtomic) connection.commit();
            st.close();

            logger.info(String.format("[NEW KUDO ENTRY CREATED] user: %d | route: %s | modifier: %d",
                    instance.getUser(),
                    instance.getRoute(),
                    instance.getModifier()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (isAtomic) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            idOfAddedInstance = new long[]{SQLERROR}; // Set error status
        }

        return idOfAddedInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(KudoEntry instance) {
        return save(instance, true);
    }

    /**
     * {@inheritDoc}
     * <p></p>
     * The only modifiable field is the vote modifier, which indicates whether it's a downvote or upvote
     */
    @Override
    public boolean save(KudoEntry instance, boolean isAtomic) {
        if (!dependenciesConfigured()) return false;

        boolean updateSuccessful = false;

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("UPDATE routekudosregistry SET modifier = %d WHERE user = %d AND route = %d",
                    instance.getModifier(),
                    instance.getUser(),
                    instance.getRoute()));

            if (isAtomic) connection.commit();
            updateSuccessful = true;
            st.close();

            logger.info(String.format("[KUDO ENTRY UPDATED] user: %d | route: %s | modifier: %d",
                    instance.getUser(),
                    instance.getRoute(),
                    instance.getModifier()));
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

        return updateSuccessful;
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
            st.executeUpdate(String.format("DELETE FROM routekudosregistry WHERE user = %d AND route = %d", id[0], id[1]));

            if (isAtomic) connection.commit();
            deletionSuccessful = true;
            st.close();

            logger.info("[Kudo entry with the id (" + id[0] + ", " + id[1] + ") has been deleted]");
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
    public Map<Long, KudoEntry> getRouteIDMappedKudoEntriesForUser(long userId) {
        List<KudoEntry> kudoEntries = getAllByUser(userId);

        if (kudoEntries == null) return null; // Check any kudo entry could be retrieved

        // Return the requested map of route IDs to Kudo entries by user identified by 'userId'
        return kudoEntries
                .stream()
                .collect(Collectors.toMap(KudoEntry::getRoute, kudoEntry -> kudoEntry));
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
