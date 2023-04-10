package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        HttpSession session = httpReq.getSession();

        /*
         * Exceptions to this filter:
         *
         *      Anonymous users can register as new users
         *      Get route categories
         *      Get route details
         *      Get all usernames (only usernames for the advanced filter)
         *      Filter routes
         *      Get user and route stats
         *      Get related routes of a route
         */

        boolean allowRESTRequest =
                (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/usuarios/?") && httpReq.getMethod().matches("(GET|POST)")) ||
                        (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/categoriasruta/?") && httpReq.getMethod().equals("GET")) ||
                        (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/rutas/?") && httpReq.getMethod().equals("GET")) ||
                        (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/rutas/filtro") && httpReq.getMethod().equals("GET")) ||
                        (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/(usuarios|rutas)/estadisticas") && httpReq.getMethod().equals("GET")) ||
                        (httpReq.getRequestURI().matches(httpReq.getContextPath() + "/rest/rutas/[0-9]+(/similares)?") && httpReq.getMethod().equals("GET"));

        // PERFORM AN AUTHENTICATION to check only allow logged users through this filter, else redirect to login form
        // It's possible to add exceptions as a combination of URI and HTTP METHOD

        Object loggedUser = session.getAttribute("user");

        if (loggedUser != null || allowRESTRequest)
            filterChain.doFilter(servletRequest, servletResponse);
        else
            httpResp.sendRedirect(httpReq.getContextPath() + "/Login.do");
    }

    @Override
    public void destroy() {
    }
}
