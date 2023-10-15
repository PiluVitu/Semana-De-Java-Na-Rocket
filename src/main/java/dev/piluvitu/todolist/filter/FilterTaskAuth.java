package dev.piluvitu.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.piluvitu.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    var serveletPath = request.getServletPath();

    if (serveletPath.startsWith("/tasks/")) {
      // [x]Pegar a autenticação do usuario
      var authorization = request.getHeader("Authorization");
      var user_auth_code_encoded = authorization
        .substring("Basic".length())
        .trim();
      byte[] user_auth_code_decode = Base64
        .getDecoder()
        .decode(user_auth_code_encoded);
      var user_auth_code = new String(user_auth_code_decode);
      String[] user_credentials = user_auth_code.split(":");
      String username = user_credentials[0];
      String password = user_credentials[1];
      // [x]Validar o usuário

      var user = this.userRepository.findByUsername(username);

      if (user == null) {
        response.sendError(401);
      } else {
        // [x]Validar senha
        var passwordVerify = BCrypt
          .verifyer()
          .verify(password.toCharArray(), user.getPassword());
        // [x]Liberar Passagem
        if (passwordVerify.verified) {
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
