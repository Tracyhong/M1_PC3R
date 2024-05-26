package org.pc3r.webtoontracker_server.api;

import com.google.gson.*;
import org.pc3r.webtoontracker_server.persistence.Chapter;
import org.pc3r.webtoontracker_server.persistence.Tag;
import org.pc3r.webtoontracker_server.persistence.Webtoon;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;


//source
//https://www.twilio.com/fr-fr/blog/5-requete-http-java#strongHttpClient-de-Java11strong:~:text=lisez%20la%20suite.-,HttpClient%20de%20Java%C2%A011,permet%20d%27appeler%C2%A0.get()%C2%A0quand%20on%20a%20besoin%20du%20r%C3%A9sultat.,-Il%20s%27agit%20d%27une
public class ApiMangadex {

    private final static int MAX_WEBTOON = 5590;
    private final static int MAX_CHAPTER_UPDATE = 100;
    private final static String BASE_IMG_URL = "https://uploads.mangadex.org/covers/";

    public static String getTotalPages() {
        return String.valueOf(MAX_WEBTOON / 10);
    }
    public static String getTotalPagesChapter() {
        return String.valueOf(MAX_CHAPTER_UPDATE / 5);
    }
    public static List<Chapter> getLastUpdates(int limit,int nbPage) throws IOException, InterruptedException {
        List<Chapter> updates = new ArrayList<>();
        int offset = (nbPage-1) * limit; // page 1 = page d'accueil du All
        var client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/chapter?translatedLanguage[]=en&contentRating[]=safe&originalLanguage[]=ko&order[updatedAt]=desc&limit="
                + limit+"&offset="+offset;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Chapter>> response = client.send(request, ofChapterList());
        updates = new ArrayList<>(response.body());
//        System.out.println("Success! Found " + limit + " updates :\n" + response.body());
        return updates;
    }

    public static List<Webtoon> getTopWebtoons(int limit) throws IOException, InterruptedException {
        List<Webtoon> webtoons = new ArrayList<>();
        var client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/manga?order[rating]=desc&originalLanguage[]=ko&contentRating[]=safe&limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Webtoon>> response = client.send(request, ofWebtoonList());
        webtoons = new ArrayList<>(response.body());
//        System.out.println("Success! Found the first top webtoons :\n" + webtoons);/* " + limit + "*/
        return webtoons;
    }

    public static Webtoon getRandom() throws IOException, InterruptedException {
        List<Webtoon> webtoons = new ArrayList<>();
        int random = (int) (Math.random() * MAX_WEBTOON);
//        System.out.println("random: " + random);
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/manga?originalLanguage[]=ko&contentRating[]=safe&limit=1&offset="+random+"&includes[]=cover_art,author";
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Webtoon>> response = client.send(request, ofWebtoonList());
        webtoons = new ArrayList<>(response.body());
//        System.out.println("Success! Found random webtoon :\n" + webtoons);//------------------------------------print debug
        return webtoons.get(0);
    }

    public static List<Webtoon> getAllWebtoonsFromPage(int nbPage) throws IOException, InterruptedException {
        List<Webtoon> webtoons = new ArrayList<>();
        int offset = (nbPage-1) * 10; // page 1 = page d'accueil du All
        var client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/manga?order[title]=asc&originalLanguage[]=ko&contentRating[]=safe&limit=10&offset=" + offset;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Webtoon>> response = client.send(request, ofWebtoonList());
        webtoons = new ArrayList<>(response.body());
//        System.out.println("Success! Found webtoons from offset " + offset + " :\n" + response.body());//------------------------------------print debug
        return webtoons;
    }

    public static Webtoon getInfosWebtoon(String webtoonID) throws IOException, InterruptedException {
        List<Webtoon> webtoons = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/manga/" + webtoonID + "?includes[]=cover_art";
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Webtoon>> response = client.send(request, ofWebtoonList());
        webtoons = new ArrayList<>(response.body());
        return webtoons.get(0);
    }

    public static List<Webtoon> searchWebtoon(String title) throws IOException, InterruptedException {
        List<Webtoon> webtoons = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/manga?title="+title+"&originalLanguage[]=ko&contentRating[]=safe&limit=10";
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Webtoon>> response = client.send(request, ofWebtoonList());
        webtoons = new ArrayList<>(response.body());
        return webtoons;
    }
    private static String getAuthorArtist(String authorID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/author/" + authorID;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject results = jsonResponse.getAsJsonObject("data");
        JsonObject attributes = results.getAsJsonObject("attributes");
        String author = attributes.get("name").getAsString();
        return author;
    }
    public static List<Chapter> getChapter(String mangaId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();//7b29f645-7dbf-46b1-8534-b70b5859c49a
        String url = "https://api.mangadex.org/chapter?manga="+mangaId+"&order[chapter]=desc&limit=5";//https://api.mangadex.org/manga/"+mangaId+"/aggregate";   //a modif j'ai pas bien checké ya surement un autre truc meilleur

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Chapter>> response = client.send(request, ofChapterList());
        return response.body();
    }

    //si on a pas le filename du cover art, on peut le recuperer avec cette methode
    public static String getCoverImage(String coverID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.mangadex.org/cover/" + coverID;
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(url))
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject results = jsonResponse.getAsJsonObject("data");
        JsonObject attributes = results.getAsJsonObject("attributes");
        return attributes.get("fileName").getAsString();
    }



    //Body handler to parse the response body into a list of Webtoon objects or chapters objects



    private static HttpResponse.BodyHandler<List<Chapter>> ofChapterList() {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                ApiMangadex::parseChapterList
        );
    }

    private static List<Chapter> parseChapterList(String responseBody) {
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray data = jsonResponse.getAsJsonArray("data");

        List<Chapter> chapters = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JsonObject chapterObject = data.get(i).getAsJsonObject();
            String id = chapterObject.get("id").getAsString();
            JsonObject attributes = chapterObject.getAsJsonObject("attributes");
            String chapter = attributes.get("chapter").getAsString();
            String updatedAt = attributes.get("updatedAt").getAsString();
            String webtoonId = "";
            JsonArray relationships = chapterObject.getAsJsonArray("relationships");
            for(int j = 0; j < relationships.size(); j++){
                JsonObject relationship = relationships.get(j).getAsJsonObject();
                String type = relationship.get("type").getAsString();
                if(type.equals("manga")){
                    webtoonId = relationship.get("id").getAsString();
                }
            }
            chapters.add(new Chapter(webtoonId,id, chapter, updatedAt));
        }
        return chapters;
    }

    private static HttpResponse.BodyHandler<List<Webtoon>> ofWebtoonList() {
        return responseInfo -> HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                ApiMangadex::parseWebtoonList
        );
    }

    private static List<Webtoon> parseWebtoonList(String responseBody){
//        Gson gson = new Gson();
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
        List<Webtoon> webtoons = new ArrayList<>();

            if (jsonResponse.get("data").isJsonArray()) {
                JsonArray results = jsonResponse.getAsJsonArray("data");
                for (int i = 0; i < results.size(); i++) {
                    JsonObject webtoonJson = results.get(i).getAsJsonObject();
                    Webtoon webtoon = null;
                    try {
                        webtoon = parseWebtoonFromJson(webtoonJson);
                    } catch (IOException | InterruptedException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                    webtoons.add(webtoon);
                }
            } else if (jsonResponse.get("data").isJsonObject()) {
                JsonObject webtoonJson = jsonResponse.getAsJsonObject("data");
                Webtoon webtoon = null;
                try{
                    webtoon = parseWebtoonFromJson(webtoonJson);
                } catch (IOException | InterruptedException | ParseException e) {
                    throw new RuntimeException(e);
                }
                webtoons.add(webtoon);
            }

        return webtoons;
    }
    private static Webtoon parseWebtoonFromJson(JsonObject webtoonJson) throws ParseException, IOException, InterruptedException{

        JsonObject attributes = webtoonJson.getAsJsonObject("attributes");
        Webtoon webtoon;
        try {
            String id = webtoonJson.get("id").getAsString();

            String title ; //titre en eng ou kor dans le json de l'api
            JsonObject titleObject = attributes.getAsJsonObject("title");
            if (titleObject.has("en")) {
                title = titleObject.get("en").getAsString();
            } else if (titleObject.has("ko")) {
                title = titleObject.get("ko").getAsString();
            } else {
                title = "Title not available";
            }


            String description ;
            JsonObject descriptionObject = attributes.getAsJsonObject("description");
            if (descriptionObject.has("en")) {
                description = descriptionObject.get("en").getAsString();
            } else if (descriptionObject.has("ko")) {
                description = descriptionObject.get("ko").getAsString();
            } else {
                description = "Description not available";
            }
            String createdAt = attributes.get("createdAt").getAsString();
            int year = ZonedDateTime.parse(createdAt).getYear();
            String status = attributes.get("status").getAsString();
            String createdAtString = attributes.get("updatedAt").getAsString();

            SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date fullDate = fullDateFormat.parse(createdAtString);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(fullDate);

            Date lastUpdate = dateFormat.parse(formattedDate);


            List<String> alternativeTitles = new ArrayList<>();
            JsonArray altTitles = attributes.getAsJsonArray("altTitles");
            for (JsonElement element : altTitles) {
                // Extract the alt title string for each language
                JsonObject altTitleObject = element.getAsJsonObject();
                for (String key : altTitleObject.keySet()) {
                    alternativeTitles.add(altTitleObject.get(key).getAsString());
                }
            }

            JsonArray tagsArray = attributes.getAsJsonArray("tags");

            List<Tag> tags = new ArrayList<>();

            for (JsonElement element : tagsArray) {
                // Extract id and name for each tag
                JsonObject tagObject = element.getAsJsonObject();
                String idTag = tagObject.get("id").getAsString();
                JsonObject attributesTag = tagObject.getAsJsonObject("attributes");
                String name = attributesTag.getAsJsonObject("name").get("en").getAsString();
                tags.add(new Tag(idTag, name));
            }
            List<String> author = new ArrayList<>();
            String artist = "";
            JsonArray relationships = webtoonJson.getAsJsonArray("relationships");
            String image = BASE_IMG_URL+id+"/";
            for(int j = 0; j < relationships.size(); j++){
                JsonObject relationship = relationships.get(j).getAsJsonObject();
                String type = relationship.get("type").getAsString();
                if(type.equals("cover_art")){
                    if(relationship.has("attributes")){
                        image += relationship.get("attributes").getAsJsonObject().get("fileName").getAsString();
                    }
                    else {
                        String coverID = relationship.get("id").getAsString();
                        image += getCoverImage(coverID);
                    }
                }
                if(type.equals("author")){
                    String authorName = getAuthorArtist(relationship.get("id").getAsString());
                    author.add(authorName);
                }
                if(type.equals("artist")){
                    String artistName = getAuthorArtist(relationship.get("id").getAsString());
                    artist = artistName;
                }
            }
            webtoon = new Webtoon(id, title, alternativeTitles, author, artist, description, year, status, tags, lastUpdate,image);

        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }
        return webtoon;
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println("Hello, World!");
//
//
//        // tests
//        ApiMangadex api = new ApiMangadex();
////        api.getUpdates(5);
////        api.getRandom();
////        getAuthorArtist("12c396ed-5304-4234-92d1-6690048a03fb");
//        //api.getAllWebtoonsFromPage(1);
//        //api.getTopWebtoon(10);
////        api.getLastUpdates(10);
////        api.searchWebtoon("Elec");
//        api.getTopWebtoons(10);
//    }
//
}
