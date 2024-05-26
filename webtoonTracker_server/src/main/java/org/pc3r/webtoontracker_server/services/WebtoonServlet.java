package org.pc3r.webtoontracker_server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.pc3r.webtoontracker_server.api.ApiMangadex;
import org.pc3r.webtoontracker_server.persistence.Chapter;
import org.pc3r.webtoontracker_server.persistence.Database;
import org.pc3r.webtoontracker_server.persistence.Webtoon;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.util.List;
import java.util.ArrayList;

import static org.pc3r.webtoontracker_server.api.ApiMangadex.getRandom;

@WebServlet(name = "webtoonServlet", value = "/webtoonServlet")
public class WebtoonServlet extends HttpServlet {
    private String message;
    private List<Webtoon> webtoonList;

    public void init() {

        message = "Hello World!";

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("GET WEBTOON");

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String get = request.getParameter("get");
        String id = request.getParameter("id");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        String query = request.getParameter("query");

        if(id != null) { //si on demande un webtoon spécifique
            if(get!=null && get.equals("chapter")){  //si on demande les chapitres d'un webtoon
                System.out.println("GET CHAPTER---------------------------------------------------------");
                List< Chapter> chapters;
                try {
                    chapters = ApiMangadex.getChapter(id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String sendMSG = gson.toJson(chapters);

                out.print(sendMSG);
                response.setStatus(200);
            }
            else {
                System.out.println("ID: " + id + "---------------------------------------------------------");
                try {
                    Webtoon webtoon = ApiMangadex.getInfosWebtoon(id);
                    String sendMSG = gson.toJson(webtoon);
//                    System.out.println("SEND to client : " + sendMSG);
                    out.print(sendMSG);
                    response.setStatus(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        if(get != null) {
            if(get.equals("random")){  //si on demande un webtoon random
                System.out.println("GET RANDOM---------------------------------------------------------");
                Webtoon webtoon;
                try {
                    webtoon = ApiMangadex.getRandom();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String sendMSG = gson.toJson(webtoon);

                out.print(sendMSG);
                response.setStatus(200);
            }
            if (get.equals("all")) { // si on demande tous les webtoons
                System.out.println("GET ALL---------------------------------------------------------");
                List<Webtoon> webtoons;
                JsonObject jsonAll = new JsonObject();
                try {
                    int p = Integer.parseInt(page);
                    webtoons = ApiMangadex.getAllWebtoonsFromPage(p);

                    jsonAll.addProperty("totalPages", ApiMangadex.getTotalPages());
                    jsonAll.add("webtoons", gson.toJsonTree(webtoons));

                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (InterruptedException | NumberFormatException e) {
                    jsonAll.addProperty("error", "Failed to fetch webtoons: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                response.getWriter().print(gson.toJson(jsonAll));
            }
            if (get.equals("lastUpdates")) { //si on demande les limit derniers webtoon updates avec une limite
                System.out.println("GET LAST UPDATES---------------------------------------------------------");
                List<Chapter> updates;
                try {
                    int lim = Integer.parseInt(limit);
                    int p = Integer.parseInt(page);
                    updates = ApiMangadex.getLastUpdates(lim, p);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                List<JsonObject> listJsonUpdates = new ArrayList<>();
                for (Chapter chapter : updates) {
                    JsonObject jsonUpdate = new JsonObject();
                    Webtoon webtoon;
                    try {
                        webtoon = ApiMangadex.getInfosWebtoon(chapter.getWebtoonId());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    jsonUpdate.addProperty("chapter", chapter.getChapter());
                    jsonUpdate.addProperty("webtoonId", webtoon.getId());
                    jsonUpdate.addProperty("title", webtoon.getTitle());
                    jsonUpdate.addProperty("image", webtoon.getImage());

                    listJsonUpdates.add(jsonUpdate);
                }

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("totalPages", ApiMangadex.getTotalPagesChapter());
                jsonResponse.add("webtoons", gson.toJsonTree(listJsonUpdates));

                out.print(gson.toJson(jsonResponse));
                response.setStatus(200);
            }
            if(get.equals("search")){  //si on demande une recherche de webtoon par titre
                System.out.println("GET SEARCH---------------------------------------------------------");
                List<Webtoon> webtoons;
                try {
                    webtoons = ApiMangadex.searchWebtoon(query);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String sendMSG = gson.toJson(webtoons);

                out.print(sendMSG);
                response.setStatus(200);
            }
            if(get.equals("top")){  //si on demande les top webtoons
                System.out.println("GET TOP---------------------------------------------------------");
                List<Webtoon> webtoons;

                try {
                    int lim = Integer.parseInt(limit);
                    webtoons = ApiMangadex.getTopWebtoons(lim);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                List<JsonObject> topWebtoons  = new ArrayList<>();
                for (Webtoon webtoon : webtoons) {
                    JsonObject jsonUpdate = new JsonObject();
                    jsonUpdate.addProperty("webtoonId", webtoon.getId());
                    jsonUpdate.addProperty("title", webtoon.getTitle());

                    topWebtoons.add(jsonUpdate);
                }

                String sendMSG = gson.toJson(topWebtoons);
                out.print(sendMSG);
                response.setStatus(200);
            }
            if(get.equals("baniere")){  //si on demande la baniere on fait des randoms n fois en fonction de la limite donnée
                System.out.println("GET BANIERE---------------------------------------------------------");
                List<Webtoon> webtoons = new ArrayList<>();
                try {
                    int lim = Integer.parseInt(limit);
                    for (int i = 0; i < lim; i++) {
                        Webtoon webtoon = getRandom();
                        webtoons.add(webtoon);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String sendMSG = gson.toJson(webtoons);

                out.print(sendMSG);
                response.setStatus(200);
            }
        }

        out.flush();
        out.close();
    }

    //pour ajouter ou supprimer un webtoon de la liste des favoris de l'utilisateur
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("PUT WEBTOON --------");

        BufferedReader reader = request.getReader();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(reader).getAsJsonObject();
        String requestType = jsonRequest.get("type").getAsString();
        String userId = jsonRequest.get("userId").getAsString();
        String webtoonId = jsonRequest.get("webtoonId").getAsString();

        Gson gson = new Gson();
        if(requestType.equals("add")) {
            Database.getInstance().addWebtoon(userId, webtoonId);
//            System.out.println("Webtoon " + webtoonId + " added to user " + userId);
        } else if(requestType.equals("delete")) {
            Database.getInstance().removeWebtoon(userId, webtoonId);
//            System.out.println("Webtoon " + webtoonId + " removed from user " + userId);
        }

        JsonObject jsonResponse = new JsonObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        jsonResponse.addProperty("status", "ok");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
        out.flush();
        out.close();
    }
    public void destroy() {
    }
}