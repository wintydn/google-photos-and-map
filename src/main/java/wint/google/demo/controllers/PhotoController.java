package wint.google.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Updates;
import wint.google.demo.auth.AppAuth;
import wint.google.demo.auth.AppAuth.Unauthenticated;

@RestController
@RequestMapping("/api")
public class PhotoController {

   @Value("${mongodb.connection.string}")
   private String mongoDbConStr;

   @Value("${mongodb.db.name}")
   private String mongoDbName;

   private String collectionName = "app_data";

   private MongoClient getMongoClient() {
      var connectionString = new ConnectionString(mongoDbConStr);
      var mongoSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
            .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
      return MongoClients.create(mongoSettings);
   }

   @GetMapping("/albums")
   List<Map<String, Object>> albums(HttpServletRequest req) throws IOException, Unauthenticated {
      var user = AppAuth.getUser(req);
      var api = AppAuth.newAPI(req);

      var client = getMongoClient();
      var template = new MongoTemplate(client, mongoDbName);

      var q = new Query().addCriteria(Criteria.where("email").is(user.getEmail()));
      q.fields().exclude("_id");
      var dbItems = template.find(q, Document.class, collectionName);

      var respBody = new ArrayList<Map<String, Object>>();
      String nextPage = null;

      do {
         var albumsResp = api.getAlbums();
         nextPage = (String) albumsResp.get("nextPageToken");
         var albums = (List<Map<String, Object>>) albumsResp.get("albums");

         if (albums != null) {
            for (var item : albums) {
               var dbItem = dbItems.stream()
                     .filter(xx -> StringUtils.equals(xx.getString("albumId"), item.get("id")))
                     .findFirst();

               if (dbItem.isPresent()) {
                  item.put("lat", dbItem.get().get("lat"));
                  item.put("lng", dbItem.get().get("lng"));
                  respBody.add(item);
               }
            }
         }
      } while (!StringUtils.isEmpty(nextPage));

      return respBody;
   }

   @PostMapping("/album/media-items")
   Object getMediaItems(HttpServletRequest req, @RequestBody Map<String, String> param)
         throws Unauthenticated, JsonProcessingException {
      var api = AppAuth.newAPI(req);
      return api.getMediaItems(param.get("albumId"), 3, param.get("nextPageToken"));
   }

   @PostMapping("/create-album")
   Object createAlbum(HttpServletRequest req, @RequestParam("albumName") String albumName,
         @RequestParam("lat") Double lat, @RequestParam("lng") Double lng)
         throws IOException, Unauthenticated {
      var user = AppAuth.getUser(req);
      var token = new AccessToken(user.getToken(), null);

      var client = getMongoClient();
      var template = new MongoTemplate(client, mongoDbName);
      var collection = template.getCollection(collectionName);

      var credentials = GoogleCredentials.newBuilder().setAccessToken(token).build();
      var settings = PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

      ClientSession session = client.startSession();
      try (var photosLibraryClient = PhotosLibraryClient.initialize(settings)) {
         session.startTransaction();

         var dbResult = collection.insertOne(new Document("email", user.getEmail())
               .append("lat", lat).append("lng", lng).append("albumId", ""));
         var inserted = dbResult.getInsertedId();

         var album = photosLibraryClient.createAlbum(albumName);
         var albumId = album.getId();

         collection.updateOne(new Document("_id", inserted), Updates.set("albumId", albumId));
         session.commitTransaction();
      } catch (ApiException e) {
         e.printStackTrace();
         session.abortTransaction();
      } finally {
         session.close();
      }

      return null;
   }

   @PostMapping("/upload")
   Object upload(HttpServletRequest req, @RequestParam("file") MultipartFile file,
         @RequestParam("albumId") String albumId) throws IOException, Unauthenticated {
      var api = AppAuth.newAPI(req);
      var uploadToken = api.upload(file.getBytes());

      var obj = new HashMap<String, Object>();
      obj.put("albumId", albumId);

      var simpleMediaItem = new HashMap<>();
      simpleMediaItem.put("uploadToken", uploadToken);
      simpleMediaItem.put("fileName", file.getOriginalFilename());

      var item = new HashMap<>();
      item.put("simpleMediaItem", simpleMediaItem);

      obj.put("newMediaItems", Arrays.asList(item));
      return api.createMediaItem(obj);
   }

}
