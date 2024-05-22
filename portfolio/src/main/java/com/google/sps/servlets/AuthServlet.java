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
        response.setContentType("application/json");

        UserService userService = UserServiceFactory.getUserService();
        AuthResponse authResponse;

        if (userService.isUserLoggedIn()) {
            User currentUser = userService.getCurrentUser();
            String userEmail = currentUser.getEmail();
            String urlToRedirectToAfterUserLogsOut = "/";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
         // update the first to use username eventually after I create prompt in
            authResponse = new AuthResponse("logged in", logoutUrl, userEmail, true, userEmail);
        } else {
            String urlToRedirectToAfterUserLogsIn = "/";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            authResponse = new AuthResponse("not logged in", loginUrl, "", false, "");
        }

        String jsonResponse = gson.toJson(authResponse);
        response.getWriter().println(jsonResponse);
        response.getWriter().close();
    }

        private static class AuthResponse {
        private String status;
        private String redirectLink;
        private String username;
        private String email;
        private boolean isLoggedIn;

        public AuthResponse(String status, String redirectLink, String username, boolean isLoggedIn, String email) {
            this.status = status;
            this.redirectLink = redirectLink;
            this.username = username;
            this.email = email;
            this.isLoggedIn = isLoggedIn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRedirectLink() {
            return redirectLink;
        }

        public void setRedirectLink(String redirectLink) {
            this.redirectLink = redirectLink;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isLoggedIn() {
            return isLoggedIn;
        }

        public void setLoggedIn(boolean isLoggedIn) {
            this.isLoggedIn = isLoggedIn;
        }

        @Override
        public String toString() {
            return "AuthResponse{" +
                    "status='" + status + '\'' +
                    ", email ='" + email + '\'' +
                    ", redirectLink='" + redirectLink + '\'' +
                    ", username='" + username + '\'' +
                    ", isLoggedIn=" + isLoggedIn +
                    '}';
        }
    }
}
