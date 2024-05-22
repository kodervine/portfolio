package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


@WebServlet("/account")
public class AuthServlet extends HttpServlet {
private Gson gson = new Gson();
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    String json = gson.toJson()
    if (userService.isUserLoggedIn()) {
        User currentUser = userService.getCurrentUser();
        String userEmail = currentUser.getEmail();
        String urlToRedirectToAfterUserLogsOut = "/";
        String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
    
        response.getWriter().println("<p>Welcome, " + userEmail + "!</p>");
        response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
        String urlToRedirectToAfterUserLogsIn = "/login";
        String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

        response.getWriter().println("<p>Hello stranger.</p>");
        response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
    response.getWriter().close();
   }
}
