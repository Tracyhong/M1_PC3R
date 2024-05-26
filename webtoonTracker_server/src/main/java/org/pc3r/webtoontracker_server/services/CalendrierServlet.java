package org.pc3r.webtoontracker_server.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.pc3r.webtoontracker_server.persistence.Chapter;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.pc3r.webtoontracker_server.api.ApiMangadex.getChapter;

@WebServlet(name = "CalendrierServlet", value = "/calendrierServlet")
public class CalendrierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    }

    // POST request to get the chapters of a list of the fav webtoons for a user
    // données utilisés pour les event du calendrier
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POST CalendrierServlet---------------------------------------------------------");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        BufferedReader reader = request.getReader();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(reader).getAsJsonObject();
        JsonArray webtoonIds = jsonRequest.get("webtoonIds").getAsJsonArray();
//        System.out.println(webtoonIds);
        PrintWriter out = response.getWriter();
        if (webtoonIds == null || webtoonIds.size() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"No webtoon IDs provided\"}");
        }else {
            List<Chapter> allChapters = new ArrayList<>();
            for (int j = 0; j < webtoonIds.size(); j++) {
                String webtoonId = webtoonIds.get(j).getAsString();
                List<Chapter> chapters = null;
                try {
                    chapters = getChapter(webtoonId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                allChapters.addAll(chapters);
            }
            // Convert list of chapters to JSON
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(allChapters);
            out.print(jsonResponse);
        }

        out.flush();
    }
}
 
