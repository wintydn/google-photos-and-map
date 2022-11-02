package wint.google.demo.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import wint.google.demo.auth.AppAuth;
import wint.google.demo.auth.AppAuth.AppUser;
import wint.google.demo.auth.AppAuth.Unauthenticated;

@Controller
public class AppController {
   @Value("${google.apis.client.id}")
   private String gApiClientId;

   @GetMapping("/app")
   public String App(HttpServletRequest req) {
      return "app";
   }

   @GetMapping("/login")
   public String Login(Model model) {
      model.addAttribute("gApiClientId", gApiClientId);
      return "login";
   }

   @PostMapping(value = "/login")
   String login(HttpServletRequest req, HttpServletResponse resp,
         @RequestPart("accessToken") String accessToken) throws IOException {
      var userInfo = AppAuth.getUserInfo(accessToken);
      AppAuth.saveUser(req, resp, userInfo);
      return "redirect:/app";
   }
}
