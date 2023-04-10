package routefilter;

import model.Route;
import model.User;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Builder of SQL route filters. To obtain the built SQL filter call {@link #buildFilter}
 *
 * @see SQLRouteFilter
 * @see RouteFilterBuilder
 */
public class SQLRouteFilterBuilder implements RouteFilterBuilder<SQLRouteFilter> {

    Set<String> whereConstraints;
    Set<Long> excludedRoutes;
    String orderConstraint;
    int limitConstraint;

    public SQLRouteFilterBuilder() {
        whereConstraints = new HashSet<>();
        excludedRoutes = new HashSet<>();
        limitConstraint = -1; // No constraint
    }

    /**
     * Search for routes matching a literal sentence in the title or description
     *
     * @param literalString Literal search string to match
     * @return This builder
     */
    public SQLRouteFilterBuilder titleOrDescriptionLiterallyContains(String literalString) {
        // Check that the search string contains some text
        if (literalString == null || literalString.trim().isEmpty()) return this;

        whereConstraints.add(String.format(
                "instr(title, '%s') OR instr(description, '%s')", literalString, literalString));

        return this;
    }

    /**
     * Search for routes which contain, at least, one of the many keywords provided
     *
     * @param keywords List of keywords to match
     * @return This builder
     */
    public SQLRouteFilterBuilder titleOrDescriptionContains(List<String> keywords) {

        // Check at least one keyword was provided

        if (keywords == null || keywords.size() == 0) return this;

        StringBuilder partialQuery = new StringBuilder();
        Iterator<String> keywordsIt = keywords.iterator();
        String currentKeyword;

        // Add clause to search for each keyword either in the title, in the description or both

        partialQuery.append("(");
        while (keywordsIt.hasNext()) {
            currentKeyword = keywordsIt.next();
            partialQuery.append(String.format("instr(title, '%s') OR instr(description, '%s')", currentKeyword, currentKeyword));

            if (keywordsIt.hasNext())
                partialQuery.append(" OR ");
        }
        partialQuery.append(")");

        whereConstraints.add(partialQuery.toString());

        return this;
    }

    /**
     * Search for routes within a distance range defined by a delta value. This range is calculated as the interval
     * [baseline - delta, baseline + delta], both ends included.
     * <p>
     * The baseline must be above 0. Else this operation will have no effect on the filter.
     * Only the absolute value of the the difference will be used
     *
     * @param baseline Base distance measure. Greater than 0
     * @param delta    Value to be subtracted and added to the baseline to create a searchable distance range
     * @return This builder
     */
    public SQLRouteFilterBuilder ofDistanceDelta(int baseline, int delta) {

        // Check that the baseline is above 0

        if (baseline > 0) {
            whereConstraints.add(
                    String.format("distance BETWEEN %d - %d AND %d + %d",
                            baseline, Math.abs(delta), baseline, Math.abs(delta)));
        }

        return this;
    }

    /**
     * Searches for routes withing the distance range given by [lowerLimit, upperLimit], both ends included.
     * Both limits should be positive integers, but if a value of -1 is supplied to a limit then that end is unbounded,
     * as in (-inf, upperLimit) or (lowerLimit, +inf)
     *
     * @param lowerLimit Lower distance limit. -1 to make it unbound
     * @param upperLimit Upper distance limit. -1 to make it unbound
     * @return This builder
     */
    public SQLRouteFilterBuilder ofDistanceRange(int lowerLimit, int upperLimit) {

        // Check whether the range is bounded or unbounded, and from which bound

        if (lowerLimit > 0 && lowerLimit <= upperLimit) { // Range filter
            whereConstraints.add(String.format("distance BETWEEN %d AND %d", lowerLimit, upperLimit));
        } else if (lowerLimit > 0 && upperLimit <= 0) { // Unbounded at the upper limit
            whereConstraints.add(String.format("distance >= %d", lowerLimit));
        } else if (lowerLimit <= 0 && upperLimit > 0) { // Unbounded at the lower limit
            whereConstraints.add(String.format("distance <= %d", upperLimit));
        }

        return this;
    }

    /**
     * Search for routes with a specific skill level
     *
     * @param skillLevel Desired skill level
     * @return This builder
     */
    public SQLRouteFilterBuilder ofSkillLevel(RouteSkillLevel skillLevel) {

        switch (skillLevel) {
            case EASY:
                whereConstraints.add("skill_level = 'facil'");
                break;
            case MEDIUM:
                whereConstraints.add("skill_level = 'media'");
                break;
            case HARD:
                whereConstraints.add("skill_level = 'dificil'");
                break;
        }

        return this;
    }

    /**
     * Search for routes that fit all the provided categories, it's an exclusive operation.
     *
     * @param routeCategories Route category names
     * @return This builder
     */
    public SQLRouteFilterBuilder ofCategories(String... routeCategories) {
        StringBuilder clause = new StringBuilder();
        Iterator<String> categoriesIt = Stream.of(routeCategories).iterator();

        // Build WHERE clause based on the number of route categories provided

        while (categoriesIt.hasNext()) {
            clause.append("categories LIKE '%").append(categoriesIt.next()).append("%'");
            if (categoriesIt.hasNext())
                clause.append(" AND ");
        }

        if (clause.length() > 0) whereConstraints.add(clause.toString());

        return this;
    }

    /**
     * Set the ordering of the routes by number of kudos either by ascending or descending order
     *
     * @param descending A true value indicates descending order, and a false value ascending order
     * @return This builder
     */
    public SQLRouteFilterBuilder orderByKudos(boolean descending) {
        orderConstraint = "kudos " + (descending ? "DESC" : "");
        return this;
    }

    /**
     * Search for routes with a minimum number of kudos and above
     *
     * @param minimumKudos Minimum number of kudos
     * @return This builder
     */
    public SQLRouteFilterBuilder minimumKudos(int minimumKudos) {
        whereConstraints.add("kudos >= " + minimumKudos);
        return this;
    }

    /**
     * Filter out blocked routes
     *
     * @return This builder
     */
    public SQLRouteFilterBuilder hideBlockedRoutes() {
        whereConstraints.add("blocked = 0");
        return this;
    }

    /**
     * Search only for routes that were created by a specific user identifies by his id
     *
     * @param userId The ID of the user
     * @return This builder
     */
    public SQLRouteFilterBuilder byUser(long userId) {
        // Check if the user ID is valid
        if (!User.validateID(userId)) return this;

        whereConstraints.add("created_by_user = " + userId);

        return this;
    }

    /**
     * Exclude routes from any result the equivalent filter would retrieve
     *
     * @param routeIds A variable number route IDs, which will exclude those routes from any result
     * @return This filter
     */
    public SQLRouteFilterBuilder exclude(long... routeIds) {

        // Add all ids to the exclusion set

        for (Long routeId : routeIds) {

            // If the route is valid add it to the exclusion set

            if (Route.validateID(routeId))
                excludedRoutes.add(routeId);
        }

        return this;
    }

    /**
     * Limit the number of routes this filter will retrieve at most
     *
     * @param limit Maximum number of routes
     * @return This builder
     */
    public SQLRouteFilterBuilder limit(int limit) {

        if (limit > 0) limitConstraint = limit;

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLRouteFilter buildFilter() {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM routes_expandedinfo");

        Iterator<String> whereConstraintsIt = this.whereConstraints.iterator();
        Iterator<Long> excludedRoutesIt = this.excludedRoutes.iterator();

        // Add WHERE clauses

        if (whereConstraintsIt.hasNext())
            sqlQuery.append(" WHERE ");

        while (whereConstraintsIt.hasNext()) {
            sqlQuery.append(whereConstraintsIt.next());
            if (whereConstraintsIt.hasNext())
                sqlQuery.append(" AND ");
        }

        // Exclude routes

        if (excludedRoutes.size() > 0) {
            // Add WHERE clause if there wasn't any previous one
            sqlQuery.append(whereConstraints.size() == 0 ? " WHERE " : " AND ").append("id NOT IN (");

            while (excludedRoutesIt.hasNext()) {
                sqlQuery.append(excludedRoutesIt.next());
                if (excludedRoutesIt.hasNext())
                    sqlQuery.append(", ");
            }

            sqlQuery.append(")");
        }

        // Add ORDER BY clause

        if (orderConstraint != null && !orderConstraint.isEmpty())
            sqlQuery.append(" ORDER BY ").append(orderConstraint);

        // Add LIMIT clause

        if (limitConstraint > 0)
            sqlQuery.append(" LIMIT ").append(limitConstraint);

        // Return the built query

        return new SQLRouteFilter(sqlQuery.toString() + ";");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        whereConstraints.clear();
        excludedRoutes.clear();
        orderConstraint = "";
        limitConstraint = -1;
    }
}
