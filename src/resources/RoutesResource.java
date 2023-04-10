package resources;

import dao.JDBCRouteDAO;
import dao.KudoEntryDAO;
import dao.RouteDAO;
import dao.UserDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import dao.implementations.RouteDAOImplJDBC;
import model.KudoEntry;
import model.Route;
import model.User;
import routefilter.RouteSkillLevel;
import routefilter.SQLRouteFilterBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/rutas")
public class RoutesResource {

    private static final Logger logger = Logger.getLogger(RoutesResource.class.getName());

    @Context
    ServletContext sc;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{routeId :[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Route getRouteJSON(@PathParam("routeId") long routeId, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        Route requestedRoute = routeDAO.getById(routeId); // Try retrieving the requested route

        // Check that the route could be retrieved

        if (requestedRoute == null)
            throw new WebApplicationException("No se encuentra la ruta solicitada", Response.Status.NOT_FOUND);

        logger.info("[REST] Serving route with id (" + routeId + ")");

        return requestedRoute;
    }

    @GET
    @Path("/filtro")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> executeRouteFilterJSON(
            @QueryParam("buscarTexto") String searchText,
            @DefaultValue("no-ordenar") @QueryParam("ordenarPorKudos") String routeKudosOrdering,
            @QueryParam("kudosMinimos") int minimumKudos,
            @DefaultValue("false") @QueryParam("ocultarRutasBloq") String hideBlockedRoutesSource,
            @DefaultValue("false") @QueryParam("mostrarMisrutas") String showOnlyMyRoutesSource,
            @QueryParam("filtroDificultad") int skillLevel,
            @QueryParam("filtrarUsuario") String filterByUsername,
            @DefaultValue("-1") @QueryParam("distanciaMinima") int minDistance,
            @DefaultValue("-1") @QueryParam("distanciaMaxima") int maxDistance,
            @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);
        HttpSession session = req.getSession();

        // Validate route order based on kudos

        if (!routeKudosOrdering.matches("(no-ordenar|ascendentes|descendentes)")) {
            logger.warning("[REST] Request parameter (ordenarPorKudos) expresses an invalid ordering");
            throw new WebApplicationException("Parámetro (ordenarPorKudos) contiene un valor inválido",
                    Response.Status.BAD_REQUEST);
        }

        // If execution reaches this point the query is valid --> Apply all suitable the filters

        boolean hideBlockedRoutes = Boolean.parseBoolean(hideBlockedRoutesSource);
        boolean showOnlyMyRoutes = Boolean.parseBoolean(showOnlyMyRoutesSource);

        SQLRouteFilterBuilder sqlRouteFilterBuilder = new SQLRouteFilterBuilder();

        if (searchText != null && !searchText.trim().isEmpty()) {

            // Check if we're dealing with a list of keywords or a literal sentence to match

            if (searchText.contains(";")) { // Is a list of keywords
                List<String> keywords = Arrays.stream(searchText.split(";"))
                        .map(keyword -> keyword = keyword.trim())
                        .collect(Collectors.toList());

                sqlRouteFilterBuilder.titleOrDescriptionContains(keywords);
            } else { // Is a literal sentence
                sqlRouteFilterBuilder.titleOrDescriptionLiterallyContains(searchText);
            }
        }
        if (!routeKudosOrdering.matches("no-ordenar"))
            sqlRouteFilterBuilder.orderByKudos(routeKudosOrdering.matches("descendentes"));
        if (req.getParameter("kudosMinimos") != null)
            sqlRouteFilterBuilder.minimumKudos(minimumKudos);
        if (hideBlockedRoutes)
            sqlRouteFilterBuilder.hideBlockedRoutes();
        if (session.getAttribute("user") != null && showOnlyMyRoutes)
            sqlRouteFilterBuilder.byUser(((User) session.getAttribute("user")).getId());
        switch (skillLevel) {
            case 1:
                sqlRouteFilterBuilder.ofSkillLevel(RouteSkillLevel.EASY);
                break;
            case 2:
                sqlRouteFilterBuilder.ofSkillLevel(RouteSkillLevel.MEDIUM);
                break;
            case 3:
                sqlRouteFilterBuilder.ofSkillLevel(RouteSkillLevel.HARD);
                break;
        }
        if (filterByUsername != null && !filterByUsername.trim().isEmpty()) {
            User filteredUserModel = userDAO.getByUsername(filterByUsername.trim());
            if (filteredUserModel != null)
                sqlRouteFilterBuilder.byUser(filteredUserModel.getId());
        }
        sqlRouteFilterBuilder.ofDistanceRange(minDistance, maxDistance);

        // Execute the filter and return the filtered routes

        RouteDAOImplJDBC jdbcRouteDAO = (RouteDAOImplJDBC) routeDAO;

        return jdbcRouteDAO.executeFilter(sqlRouteFilterBuilder.buildFilter());
    }

    @GET
    @Path("/estadisticas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> routeStatisticsQueryHandlerJSON(@QueryParam("e") String requestedStat) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);

        // Validate there's a route stat being requested

        if (requestedStat == null || requestedStat.trim().isEmpty())
            throw new WebApplicationException("No se ha solicitado ninguna estadística de ruta",
                    Response.Status.BAD_REQUEST);

        logger.info("[REST] Serving route statistics (" + requestedStat + ")");

        // Get the route stat being requested

        if (requestedStat.equals("topRutasSemanal")) { // Serve the top 5 weekly routes

            List<Route> top5RoutesOfTheWeek = routeDAO.getTopRoutesOfTheWeek();

            // Get first 5 results

            top5RoutesOfTheWeek = top5RoutesOfTheWeek
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            // Set route creation date to the format of the top route card container

            top5RoutesOfTheWeek
                    .forEach(route -> route.changeDateFormat(new SimpleDateFormat("dd MMM - HH:mm")));

            return top5RoutesOfTheWeek;
        } else if (requestedStat.equals("topRutasMensual")) { // Serve the top 5 monthly routes

            List<Route> top5RoutesOfTheMonth = routeDAO.getTopRoutesOfTheMonth();

            // Get first 5 results

            top5RoutesOfTheMonth = top5RoutesOfTheMonth
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            // Set route creation date to the format of the top route card container

            top5RoutesOfTheMonth
                    .forEach(route -> route.changeDateFormat(new SimpleDateFormat("dd MMM - HH:mm")));

            return top5RoutesOfTheMonth;
        } else { // Other unhandled route stat
            return null;
        }
    }

    @GET
    @Path("{routeId : [0-9]+}/similares")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Route> relatedRoutesHandlerJSON(
            @QueryParam("por") String similarity,
            @QueryParam("limite") int limit,
            @QueryParam("deltaDistancia") int distanceDelta,
            @PathParam("routeId") long routeId) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        SQLRouteFilterBuilder sqlRouteFilterBuilder = new SQLRouteFilterBuilder();
        JDBCRouteDAO jdbcRouteDAO = (JDBCRouteDAO) routeDAO;

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // Validate similarity query param

        if (similarity == null || similarity.trim().isEmpty())
            throw new WebApplicationException("La característica de similitud no es válida",
                    Response.Status.BAD_REQUEST);

        // Get the requested route and check that it exists

        Route requestedRoute = routeDAO.getById(routeId);

        if (requestedRoute == null)
            throw new WebApplicationException("No se encuentra la ruta", Response.Status.NOT_FOUND);

        // Process the requested similarity

        logger.info("[REST] Retrieving similar routes to route with ID (" + routeId + ") by (" + similarity + ")");

        similarity = similarity.trim();
        switch (similarity) {
            case "distancia":  // Similar routes by similar distance

                // 3 Related routes by distance within a range given a distance delta (with more kudos)

                sqlRouteFilterBuilder
                        .ofDistanceDelta(requestedRoute.getDistance(), distanceDelta)
                        .orderByKudos(true)
                        .exclude(routeId); // Exclude self

                // Validate route limit query parameter, it should be a positive integer. 0 means it's not set

                if (limit > 0)
                    sqlRouteFilterBuilder.limit(limit);
                else if (limit < 0)
                    throw new WebApplicationException("El límite no puede ser un número negativo o 0",
                            Response.Status.BAD_REQUEST);

                return jdbcRouteDAO.executeFilter(sqlRouteFilterBuilder.buildFilter());

            case "dificultad":  // Similar routes by same skill level

                // 3 Related routes with the same skill level (with more kudos)

                sqlRouteFilterBuilder
                        .ofSkillLevel(RouteSkillLevel.parseSkillLevelFromString(requestedRoute.getSkillLevel()))
                        .orderByKudos(true)
                        .exclude(routeId); // Exclude self

                // Validate route limit query parameter, it should be a positive integer. 0 means it's not set

                if (limit > 0)
                    sqlRouteFilterBuilder.limit(limit);
                else if (limit < 0)
                    throw new WebApplicationException("El límite no puede ser un número negativo o 0",
                            Response.Status.BAD_REQUEST);

                return jdbcRouteDAO.executeFilter(sqlRouteFilterBuilder.buildFilter());

            case "categorias":  // Similar routes by same set of categories

                // 3 Related routes with shared route categories (with more kudos)

                sqlRouteFilterBuilder
                        .ofCategories(requestedRoute.getCategories().split(Route.CATEGORY_SEPARATOR))
                        .orderByKudos(true)
                        .exclude(routeId); // Exclude self

                // Validate route limit query parameter, it should be a positive integer. 0 means it's not set

                if (limit > 0)
                    sqlRouteFilterBuilder.limit(limit);
                else if (limit < 0)
                    throw new WebApplicationException("El límite no puede ser un número negativo o 0",
                            Response.Status.BAD_REQUEST);

                return jdbcRouteDAO.executeFilter(sqlRouteFilterBuilder.buildFilter());

            default:  // Unhandled similarities
                return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRouteJSON(Route newRoute, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        ArrayList<String> validationMessages = new ArrayList<>();
        HttpSession session = req.getSession();

        // Validate the route

        if (newRoute == null || !newRoute.validateFormFields(validationMessages))
            throw new WebApplicationException("La ruta proporcionada es inválida\n" + validationMessages.toString(),
                    Response.Status.BAD_REQUEST);

        User loggedUser = (User) session.getAttribute("user");

        newRoute.setCreatedByUser(loggedUser.getId()); // Set the author of this route

        // Try creating the new route

        long newRouteID = routeDAO.add(newRoute)[0];

        if (newRouteID != -1) {
            logger.info("[REST] Created new route. New ID (" + newRouteID + ")");
            return Response // New route created. Return code 201 (Created) and Location header with the new route's URI
                    .created(uriInfo
                            .getAbsolutePathBuilder()
                            .path(Long.toString(newRouteID))
                            .build())
                    .build();
        } else { // An error occurred while creating the new route
            throw new WebApplicationException("Ocurrió un error al crear la ruta proporcionada",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/{routeId : [0-9]+}/estado")
    public Response routeBlockedStateHandler(
            @PathParam("routeId") long routeId, @QueryParam("accion") String action, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        HttpSession session = req.getSession();
        boolean validAction = false;

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // Validate the requested action. It can either be a request to block or unblock a route

        if (action == null || action.trim().isEmpty() || !action.matches("(des)?bloquear"))
            throw new WebApplicationException("Acción no proporcionada o inválida", Response.Status.BAD_REQUEST);

        // Check if a route can be retrieved with the requested ID

        Route requestedRoute = routeDAO.getById(routeId);
        if (requestedRoute != null) {

            // AUTHORISATION FILER. Only the author of the route can block or unblock it

            User loggedUser = (User) session.getAttribute("user");
            if (loggedUser.getId() == requestedRoute.getCreatedByUser()) {

                /*
                 * Check the given action against the current route status. If the route is blocked and the
                 * action tells to unblock it, or the route is unblocked and the action tells to block it, it
                 * is considered a valid action.
                 */

                if (action.equals("bloquear") && !requestedRoute.isBlocked()) {
                    requestedRoute.setBlocked(true);
                    validAction = true;
                } else if (action.equals("desbloquear") && requestedRoute.isBlocked()) {
                    requestedRoute.setBlocked(false);
                    validAction = true;
                }

                // Check action validity before committing to the execution of an action

                if (validAction) {

                    boolean success = routeDAO.save(requestedRoute); // Try executing the requested action

                    if (success) {
                        logger.info("[REST] Route with ID (" + routeId + ") has been "
                                + (requestedRoute.isBlocked() ? "blocked" : "unblocked"));
                        return Response.noContent().build(); // On valid action return code 204 - No content
                    } else { // Error executing the requested action
                        throw new WebApplicationException("Ocurrió un error al actualizar el estado de bloqueo de la ruta",
                                Response.Status.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    logger.warning("[REST] Invalid route action");
                    return Response.notModified().build(); // On invalid action return code 304 - Not modified
                }
            } else { // Insufficient privileges
                throw new WebApplicationException("Este usuario no tiene permiso para modificar el estado de bloqueo de esta ruta",
                        Response.Status.UNAUTHORIZED);
            }
        } else { // Couldn't find the route at the backend
            throw new WebApplicationException("No se encuentra la ruta solicitada", Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Path("/{routeId : [0-9]+}/kudos")
    public Response routeKudosHandler(
            @PathParam("routeId") long routeId, @QueryParam("accion") String action, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        KudoEntryDAO kudoEntryDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(KudoEntry.class);
        HttpSession session = req.getSession();
        boolean kudoUpdateSuccessful = false;

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // Validate the requested action. It can either be a request to give or take the logged user's kudo given to the requested route

        if (action == null || action.trim().isEmpty() || !action.matches("(dar|quitar)"))
            throw new WebApplicationException("Acción no proporcionada o inválida", Response.Status.BAD_REQUEST);

        int equivalentKudoModifier = action.equals("dar") ? 1 : -1; // Equivalent kudo modifier for the requested action

        // Check if a route can be retrieved with the requested ID

        Route requestedRoute = routeDAO.getById(routeId);
        if (requestedRoute != null) {

            User loggedUser = (User) session.getAttribute("user");
            KudoEntry matchingKudoEntry = kudoEntryDAO.getById(loggedUser.getId(), routeId);

            // Check if the user has already given a kudo or not to the route, if not make a new kudo entry

            if (matchingKudoEntry != null) {

                /*
                 * Query the current kudo rating the user has given to this route and decide the appropriate action.
                 *
                 *    1. +1 Kudo already given AND +1 modifier --> Remove the +1 kudo
                 *    2. +1 Kudo already given AND -1 modifier --> Change kudo rating from +1 to -1
                 *    3. -1 Kudo already given AND +1 modifier --> Change the kudo rating from -1 to +1
                 *    4. -1 Kudo already given AND -1 modifier --> Remove the -1 kudo
                 */

                if (matchingKudoEntry.getModifier() == equivalentKudoModifier) { // 1. and 4.
                    kudoUpdateSuccessful = kudoEntryDAO.deleteById(loggedUser.getId(), routeId);
                } else if (matchingKudoEntry.getModifier() == 1 && equivalentKudoModifier == -1) { // 2.
                    matchingKudoEntry.setModifier(-1);
                    kudoUpdateSuccessful = kudoEntryDAO.save(matchingKudoEntry);
                } else if (matchingKudoEntry.getModifier() == -1 && equivalentKudoModifier == 1) { // 3.
                    matchingKudoEntry.setModifier(1);
                    kudoUpdateSuccessful = kudoEntryDAO.save(matchingKudoEntry);
                }
            } else {

                // User never gave any kudo to this route, create a new entry

                KudoEntry newKudoEntry = new KudoEntry();
                newKudoEntry.setUser(loggedUser.getId());
                newKudoEntry.setRoute(routeId);
                newKudoEntry.setModifier(equivalentKudoModifier);
                kudoUpdateSuccessful = kudoEntryDAO.add(newKudoEntry)[0] != -1;
            }

            if (kudoUpdateSuccessful) {
                logger.info("[REST] Successful kudo update by user ("
                        + loggedUser.getUsername() + ") to route with ID (" + routeId + ")");
                return Response.noContent().build(); // On valid kudo update return code 204 - No content
            } else { // Error registering a new kudo entry / updating an existing kudo entry at the backend
                throw new WebApplicationException("Ocurrió un error al crear o actualizar una entrada kudo",
                        Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else { // Couldn't find the route at the backend
            throw new WebApplicationException("No se encuentra la ruta solicitada", Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Path("/{routeId: [0-9]+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveRouteJSON(@PathParam("routeId") long routeId, Route uploadedRoute, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        ArrayList<String> validationMessages = new ArrayList<>();
        HttpSession session = req.getSession();

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        // Validate the uploaded route

        if (uploadedRoute == null || !uploadedRoute.validateRouteEditionAttempt(validationMessages))
            throw new WebApplicationException("La ruta proporcionada es inválida" + validationMessages.toString(),
                    Response.Status.BAD_REQUEST);

        // Check that the ID of the URI matches with the ID of the uploaded route

        if (routeId != uploadedRoute.getId()) {
            throw new WebApplicationException("La URI solicitada y el ID de la ruta proporcionado no coinciden",
                    Response.Status.FORBIDDEN);
        }

        Route storedRoute = routeDAO.getById(routeId);

        // Check if the route could be found at the backend

        if (storedRoute != null) {

            // AUTHORISATION FILTER. Only the author of the route can update it

            User loggedUser = (User) session.getAttribute("user");
            if (loggedUser.getId() == storedRoute.getCreatedByUser()) {

                // Try updating the requested route

                boolean updateSuccessful = routeDAO.save(uploadedRoute);

                if (updateSuccessful) {
                    logger.info("[REST] Updated the data of the route with ID (" + routeId + ")");
                    return Response.noContent().build(); // Return code 204 - No content
                } else { // An error occurred while updating the requested route
                    throw new WebApplicationException("Ocurrió un error al actualizar los datos de la ruta solicitada",
                            Response.Status.INTERNAL_SERVER_ERROR);
                }
            } else { // Insufficient privileges
                throw new WebApplicationException("Este usuario no tiene permisos para editar la ruta solicitada",
                        Response.Status.UNAUTHORIZED);
            }
        } else { // Route not found at the backend
            throw new WebApplicationException("No se encontró la ruta solicitada", Response.Status.NOT_FOUND);
        }
    }

    @DELETE
    @Path("/{routeId: [0-9]+}")
    public Response deleteRoute(@PathParam("routeId") long routeId, @Context HttpServletRequest req) {

        RouteDAO routeDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(Route.class);
        HttpSession session = req.getSession();

        // Validate the route ID

        if (!Route.validateID(routeId))
            throw new WebApplicationException("ID de ruta inválido", Response.Status.BAD_REQUEST);

        Route routeBeingDeleted = routeDAO.getById(routeId);

        // Check if the route could be found at the backend

        if (routeBeingDeleted != null) {

            // AUTHORISATION FILTER. Only the author of the route can delete it

            User loggedUser = (User) session.getAttribute("user");
            if (loggedUser.getId() == routeBeingDeleted.getCreatedByUser()) {

                // Try deleting the requested route

                boolean deletionSuccessful = routeDAO.deleteById(routeId);

                if (deletionSuccessful) {
                    logger.info("[REST] DELETED THE ROUTE WITH ID (" + routeId + ")");
                    return Response.noContent().build(); // Return code 204 - No content
                } else { // An error occurred while deleting the requested route
                    throw new WebApplicationException("Ocurrió un error al eliminar la ruta solicitada",
                            Response.Status.INTERNAL_SERVER_ERROR);
                }
            } else { // Insufficient privileges
                throw new WebApplicationException("Este usuario no tiene permisos para eliminar la ruta seleccionada",
                        Response.Status.UNAUTHORIZED);
            }
        } else { // Route not found
            throw new WebApplicationException("No se encontró la ruta solicitada", Response.Status.NOT_FOUND);
        }
    }
}
