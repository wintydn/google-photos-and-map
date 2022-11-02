package wint.google.demo.services;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PhotoAPI {

   private final String BASE_URL = "https://photoslibrary.googleapis.com/v1";
   private final RestTemplate rest;
   private final HttpHeaders headers;
   private final HttpEntity<String> entity;

   public PhotoAPI(String accessToken) {
      rest = new RestTemplate();
      headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);
      entity = new HttpEntity<>(headers);
   }

   public Map getAlbums() {
      var albumsUrl = BASE_URL + "/albums?fields=albums(id,title,mediaItemsCount)";
      var resp = rest.exchange(albumsUrl, HttpMethod.GET, entity, Map.class);
      return resp.getBody();
   }

   public String upload(byte[] file) {
      var uploadUrl = BASE_URL + "/uploads";
      headers.add("Content-type", "application/octet-stream");
      headers.add("X-Goog-Upload-Content-Type", "mime-type");
      headers.add("X-Goog-Upload-Protocol", "raw");
      var resp = rest.exchange(uploadUrl, HttpMethod.POST, new HttpEntity<>(file, headers),
            String.class);
      return resp.getBody();
   }

   public Object createMediaItem(Object body) throws JsonProcessingException {
      var createMediaItemUrl = BASE_URL + "/mediaItems:batchCreate";
      headers.setContentType(MediaType.APPLICATION_JSON);
      var resp = rest.exchange(createMediaItemUrl, HttpMethod.POST, new HttpEntity<>(body, headers),
            Object.class);
      return resp.getBody();
   }

   public Map getMediaItems(String albumId, int pageSize, String nextPageToken)
         throws JsonProcessingException {
      var getMediaItemUrl =
            BASE_URL + "/mediaItems:search?fields=nextPageToken,mediaItems(baseUrl)";
      headers.setContentType(MediaType.APPLICATION_JSON);

      var body = new HashMap<>();
      body.put("albumId", albumId);
      body.put("pageSize", pageSize);

      if (!StringUtils.isEmpty(nextPageToken))
         body.put("pageToken", nextPageToken);

      var resp = rest.exchange(getMediaItemUrl, HttpMethod.POST, new HttpEntity<>(body, headers),
            Map.class);
      return resp.getBody();
   }
}

