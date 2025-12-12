package com.ssafy.foody.auth.filter;

import java.io.IOException;
import java.util.Collection;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminAccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        //관리자 경로만 필터로 막기
        if (uri.startsWith("/admin")) {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // 인증이 없으면 401
            if (auth == null || !auth.isAuthenticated()) {
                writeJson(response, 401, "UNAUTHORIZED", "로그인이 필요합니다.", uri);
                return;
            }

            // ROLE_ADMIN이 아니면 403
            if (!hasRoleAdmin(auth.getAuthorities())) {
                writeJson(response, 403, "FORBIDDEN ", "관리자만 접근이 필요합니다.", uri);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean hasRoleAdmin(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) return false;
        return authorities.stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    private void writeJson(HttpServletResponse response,
                           int status,
                           String code,
                           String message,
                           String path) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String body = """
                {"code":"%s","message":"%s","path":"%s"}
                """.formatted(code, message, path);

        response.getWriter().write(body);
    }
}
