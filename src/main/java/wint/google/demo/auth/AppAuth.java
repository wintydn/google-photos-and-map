package wint.google.demo.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import wint.google.demo.services.PhotoAPI;

public class AppAuth {
   protected static final String SESSION_TOKEN = "session_token";
   private static final String PRIVATE_KEY = "test1234";
   private static final String ISSUER = "test-issuer";
   private static final String APP_USER = "app-user";

   private final static String GOOGLE_PROFILE_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

   public static AppUser getUserInfo(String accessToken)
         throws JsonMappingException, JsonProcessingException {
      var uri = UriComponentsBuilder.fromHttpUrl(GOOGLE_PROFILE_URL)
            .queryParam("access_token", accessToken).build().toUriString();

      var rest = new RestTemplate();
      var mapper = new ObjectMapper();

      var resp = rest.getForObject(uri, String.class);
      var json = mapper.readTree(resp);
      var email = json.get("email").textValue();
      var name = json.get("name").textValue();

      return new AppUser() {
         {
            setEmail(email);
            setName(name);
            setToken(accessToken);
         }
      };
   }

   public static AppUser getUser(HttpServletRequest req) throws Unauthenticated {
      try {
         for (var cookie : req.getCookies())
            if (cookie.getName().equals(SESSION_TOKEN)) {
               var mapper = new ObjectMapper();
               var token = cookie.getValue();
               var algorithm1 = Algorithm.HMAC256(PRIVATE_KEY);
               var verifier = JWT.require(algorithm1).withIssuer(ISSUER).build();
               var jwt = verifier.verify(token);
               var claims = jwt.getClaims();
               var raw = claims.get(APP_USER).asString();
               return mapper.readValue(raw, AppUser.class);
            }

         return null;
      } catch (Exception e) {
         throw new Unauthenticated();
      }
   }

   public static void throwIfNotAuthenticated(HttpServletRequest req) throws Unauthenticated {
      AppUser appUser = null;

      try {
         var user = getUser(req);
         appUser = getUserInfo(user.getToken());
      } catch (Exception e) {
         appUser = null;
      }

      if (appUser == null)
         throw new Unauthenticated();
   }

   public static void saveUser(HttpServletRequest req, HttpServletResponse resp, AppUser user)
         throws JsonProcessingException {
      var mapper = new ObjectMapper();
      var algorithm = Algorithm.HMAC256(PRIVATE_KEY);
      var token = JWT.create().withIssuer(ISSUER)
            .withClaim(APP_USER, mapper.writeValueAsString(user)).sign(algorithm);
      var cookie = new Cookie(SESSION_TOKEN, token);
      resp.addCookie(cookie);
   }

   public static PhotoAPI newAPI(HttpServletRequest req) throws Unauthenticated {
      var user = AppAuth.getUser(req);
      return new PhotoAPI(user.getToken());
   }

   public static class Unauthenticated extends Exception {
      private static final long serialVersionUID = 1L;
   }

   public static class AppUser {
      private String email;
      private String name;
      private String token;

      public String getEmail() {
         return email;
      }

      public void setEmail(String email) {
         this.email = email;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getToken() {
         return token;
      }

      public void setToken(String token) {
         this.token = token;
      }
   }
}
