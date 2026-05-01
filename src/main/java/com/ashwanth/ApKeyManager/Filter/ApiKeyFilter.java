package com.ashwanth.ApKeyManager.Filter;

import com.ashwanth.ApKeyManager.Model.API;
import com.ashwanth.ApKeyManager.Repository.APIRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final int DAILY_LIMIT = 100;

    private final APIRepository apiRepository;

    public ApiKeyFilter(APIRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Protect only /fact endpoints
       if (path.startsWith("/fact") || path.startsWith("/generate-qr")) {

            // Allow preflight requests
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }


            String apiKey = request.getHeader("x-api-key");

            // Missing or blank API key
            if (apiKey == null || apiKey.isBlank()) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "invalid_api_key", "API key is invalid or inactive.");
                return;
            }

            // Lookup key
            API api = apiRepository.findByApiKeyAndActiveTrue(apiKey);

            if (api == null) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "invalid_api_key", "API key is invalid or inactive.");
                return;
            }

            // Reset daily counter at system date boundary
            if (api.getLastReset() == null ||
                    api.getLastReset().toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {

                api.setRequestCount(0);
                api.setLastReset(LocalDateTime.now());
            }

            // Rate limit check
            if (api.getRequestCount() >= DAILY_LIMIT) {
                sendJsonError(response, 429, "rate_limit_exceeded", "Daily limit reached for this API key. Try again tomorrow or upgrade your plan.");
                // Log for debugging
                log.info("API KEY USED: {} ENDPOINT: {} COUNT: {}/{}", apiKey, path, api.getRequestCount(), DAILY_LIMIT);
                return;
            }

            // Increment and persist
            api.setRequestCount(api.getRequestCount() + 1);
            apiRepository.save(api);

            // Logging
            log.info("API KEY USED: {} ENDPOINT: {} COUNT: {}/{}", apiKey, path, api.getRequestCount(), DAILY_LIMIT);
        }

        filterChain.doFilter(request, response);
    }

    private void sendJsonError(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\":\"%s\",\"message\":\"%s\"}", error, message));
    }
}