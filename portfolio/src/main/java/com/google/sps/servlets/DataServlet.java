// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet { 
    Query query = new Query("Comment");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();
    private List<CommentResponse> dataList = new ArrayList<>();

    @Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String maxCommentsParam = request.getParameter("maxComments");
    int maxComments = 3;
    try {
        maxComments = Integer.parseInt(maxCommentsParam);
    } catch (NumberFormatException e) {
        maxComments = 3;
    }

    dataList.clear();

    User currentUser = userService.getCurrentUser();
    PreparedQuery results = datastore.prepare(query);
    int commentCount = 0;
    for (Entity entity : results.asIterable()) {
        if (commentCount >= maxComments) {
            break; 
        }
        String commentText = (String) entity.getProperty("commentText");
        String username = currentUser.getEmail();
        // String username = (String) entity.getProperty("username");
        String userEmail = currentUser.getEmail();
  
        CommentResponse commentResponse = new CommentResponse(username, userEmail, commentText);
        
        dataList.add(commentResponse);
        commentCount++;
    }

    Gson gson = new Gson();
    String json = gson.toJson(dataList);

    response.setContentType("application/json;");
    response.getWriter().println(json);
}

  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
     String username = request.getParameter("username");
     String userEmail = request.getParameter("userEmail");
     String commentText = request.getParameter("text-input");

     CommentResponse commentResponse = new CommentResponse(username, userEmail, commentText);

     Entity commentEntity = new Entity("Comment");
     commentEntity.setProperty("username", commentResponse.getUsername());
     commentEntity.setProperty("userEmail", commentResponse.getUserEmail());
     commentEntity.setProperty("commentText", commentResponse.getComment());
     datastore.put(commentEntity);

     response.sendRedirect("/");
   }

   private static class CommentResponse {
    private String username;
    private String userEmail;
    private String comment;

    public CommentResponse(String username, String userEmail, String comment) {
        this.username = username;
        this.userEmail = userEmail;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
                "username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}

}
