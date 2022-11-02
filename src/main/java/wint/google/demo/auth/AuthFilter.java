package wint.google.demo.auth;

import org.springframework.stereotype.Component;
import wint.google.demo.auth.AppAuth.Unauthenticated;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

@Component
public class AuthFilter implements Filter {

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
         throws IOException, ServletException {
      var req = (HttpServletRequest) request;
      var res = (HttpServletResponse) response;
      var reqPath = req.getServletPath();

      try {
         if (!(reqPath.equalsIgnoreCase("/login") ||
               reqPath.startsWith("/js/") ||
               reqPath.startsWith("/css/"))) {
            AppAuth.throwIfNotAuthenticated(req);
         }
         chain.doFilter(request, response);
      } catch (Unauthenticated e) {
         if (reqPath.startsWith("/api/")) {
            res.setStatus(401);
         } else {
            res.sendRedirect("/login");
         }
      } catch (Exception e) {
         System.out.println(e.getStackTrace());
         throw e;
      }
   }

}
