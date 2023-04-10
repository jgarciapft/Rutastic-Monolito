package controller;

import dao.UserDAO;
import dao.factories.DAOAbstractFactory;
import dao.implementations.DAOImplJDBC;
import helper.model.ModelMapper;
import helper.model.ModelMapperFactory;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Check if the request comes from the registration form and it's his first login attempt
        String loginInfoMessage = (String) session.getAttribute("loginInfoMessage");
        if (loginInfoMessage != null) {
            req.setAttribute("loginInfoMessage", loginInfoMessage);
            session.removeAttribute("loginInfoMessage");
        }

        // Dispatch request to login form
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO userDAO = DAOAbstractFactory.get().impl(DAOImplJDBC.class).forModel(User.class);
        ModelMapper<User> userModelMapper = ModelMapperFactory.get().forModel(User.class);
        HttpSession session = req.getSession();
        User parsedUser;
        User registeredUser = null;
        boolean loginSuccessful = false;

        req.setCharacterEncoding("UTF-8");

        parsedUser = userModelMapper.parseFromQueryParams(req.getParameterMap()); // Get the user input
        if (parsedUser != null && parsedUser.validateLoginAttempt()) { // Server-side input validation
            registeredUser = userDAO.getByUsername(parsedUser.getUsername()); // Get the registered user by username
            if (registeredUser != null) {

                // Attempt to log in the user

                loginSuccessful = parsedUser.getPassword().equals(registeredUser.getPassword());
                if (loginSuccessful) { // User successfully logged in. Store it in the session and redirect the user
                    logger.info("User " + parsedUser.getUsername() + " successfully logged in");
                    session.setAttribute("user", registeredUser);

                    resp.sendRedirect(req.getContextPath() + "/index.html");
                }
            }
        }

        // On error send back to login form

        if (parsedUser == null || registeredUser == null || !loginSuccessful) {
            logger.warning("Failed login attempt");
            req.setAttribute("loginError", "loginError");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
