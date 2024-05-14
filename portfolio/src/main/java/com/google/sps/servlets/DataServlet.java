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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    ArrayList<String> dataList = new ArrayList<String>();
    
    Query query = new Query("Comment");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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

    PreparedQuery results = datastore.prepare(query);
    int commentCount = 0;
    for (Entity entity : results.asIterable()) {
        if (commentCount >= maxComments) {
            break; 
        }
        String commentText = (String) entity.getProperty("commentText");
        dataList.add(commentText);
        commentCount++;
    }

    Gson gson = new Gson();
    String json = gson.toJson(dataList);

    response.setContentType("application/json;");
    response.getWriter().println(json);
}

  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentText = request.getParameter("text-input");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("commentText", commentText);
    datastore.put(commentEntity);
  
    response.sendRedirect("/");
   }

}
