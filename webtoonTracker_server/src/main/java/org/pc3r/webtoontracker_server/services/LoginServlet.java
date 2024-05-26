package org.pc3r.webtoontracker_server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.pc3r.webtoontracker_server.persistence.Database;
import org.pc3r.webtoontracker_server.persistence.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/loginServlet")
public class LoginServlet extends HttpServlet {
    // GET request to login a user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Gson gson = new Gson();

        System.out.println("name: " + name + " password: " + password);
        System.out.println("asked for userExists");
        JsonObject jsonResponse = new JsonObject();
        if(name == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Name or password missing !");
        }
        else{
            User userExists = Database.getInstance().userExists(name, password);

//            System.out.println("Servlet : userExists: " + userExists);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (userExists != null) {
                HttpSession session = request.getSession();
//            session.setAttribute("email", email);
                session.setAttribute("name", name);
                session.setAttribute("password", password);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse.addProperty("status", "ok");

                jsonResponse.addProperty("status", "ok");

                jsonResponse.add("user", gson.toJsonTree(userExists));
//                List<String> webtoons = Database.getInstance().getWebtoons(userExists.getId());
//                jsonResponse.add("webtoons", gson.toJsonTree(webtoons));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Invalid credentials");
            }

        }
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
        out.flush();
        out.close();
    }


    // POST request to register a user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(reader).getAsJsonObject();
        String email = jsonRequest.get("email").getAsString();
        String name = jsonRequest.get("name").getAsString();
        String password = jsonRequest.get("password").getAsString();
        Gson gson = new Gson();

        PrintWriter out = response.getWriter();
        System.out.println("name: " + name + " password: " + password);
        JsonObject jsonResponse = new JsonObject();
        if (name == null || password == null || email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Name or password missing !");
        } else {

            User userExists = Database.getInstance().userExists(name, password);

//            System.out.println("Servlet : userExists: " + userExists);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (userExists == null){

                User insertUser = Database.getInstance().addUser(name, email, password);
                if(insertUser != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    jsonResponse.addProperty("status", "ok");
                    HttpSession session = request.getSession();
                    session.setAttribute("email", email);
                    session.setAttribute("name", name);
                    session.setAttribute("password", password);

                    jsonResponse.add("user", gson.toJsonTree(insertUser));
//                    List<String> webtoons = Database.getInstance().getWebtoons(insertUser.getId());
//                    jsonResponse.add("webtoons", gson.toJsonTree(webtoons));
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Internal server error");
                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Invalid credentials");
            }

        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
        out.close();
    }
}
 
