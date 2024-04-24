package ua.everybuy.authorization.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.everybuy.authorization.buisnesslogic.service.JwtServiceUtils;
import ua.everybuy.authorization.errorhandling.ExpiredTokenException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtServiceUtils jwtServiceUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            //try {
                email = jwtServiceUtils.getEmail(jwt);
//            } catch (ExpiredJwtException e) {
//                //logger.debug("Время жизни токена вышло");
//                //throw new ExpiredTokenException("Время жизни токена вышло");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Время жизни токена вышло");
//                //throw new ServletException("ServletException");
//            }
//            } catch (SignatureException e) {  //TODO
//                //logger.debug("Подпись неправильная");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный токен");
//            }
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    jwtServiceUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
