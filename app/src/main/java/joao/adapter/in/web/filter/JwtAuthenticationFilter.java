package joao.adapter.in.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import joao.adapter.out.JwtServiceAdapterOut;
import joao.core.domain.User;
import joao.core.exception.InvalidTokenException;
import joao.core.port.out.UserRepositoryPortOut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceAdapterOut jwtServiceAdapterOut;
    private final UserRepositoryPortOut userRepositoryPortOut;

    public JwtAuthenticationFilter(JwtServiceAdapterOut jwtServiceAdapterOut, UserRepositoryPortOut userRepositoryPortOut) {
        this.jwtServiceAdapterOut = jwtServiceAdapterOut;
        this.userRepositoryPortOut = userRepositoryPortOut;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = this.recoverToken(request);

        if (token != null) {
            try {
                String email = this.jwtServiceAdapterOut.validateToken(token);
                User user = this.userRepositoryPortOut.findByEmail(email).orElse(null);
                if (user != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (InvalidTokenException e) {
                // Token inválido - não definir autenticação
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
