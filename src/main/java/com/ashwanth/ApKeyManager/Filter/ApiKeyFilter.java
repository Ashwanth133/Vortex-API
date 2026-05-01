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
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final int DAILY_LIMIT = 100;
    private static final int FACT_PER_MIN = 10;
    private static final int QR_PER_MIN = 5;

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

        if (path.startsWith("/fact") || path.startsWith("/generate-qr")) {

            // Allow preflight requests
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            String apiKey = request.getHeader("x-api-key");

            // Missing or blank API key
            if (apiKey == null || apiKey.isBlank()) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "invalid_api_key", "API key is invalid or inactive.");
                return;
            }

            // Lookup key
            API api = apiRepository.findByApiKeyAndActiveTrue(apiKey);
            if (api == null) {
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "invalid_api_key", "API key is invalid or inactive.");
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            // Reset daily counter at midnight
            if (api.getLastReset() == null ||
                    api.getLastReset().toLocalDate().isBefore(now.toLocalDate())) {
                api.setRequestCount(0);
                api.setLastReset(now);
            }

            // Reset per-minute counters every 60 seconds
            if (api.getLastMinuteReset() == null ||
                    Duration.between(api.getLastMinuteReset(), now).toSeconds() >= 60) {
                api.setFactRequestCount(0);
                api.setQrRequestCount(0);
                api.setLastMinuteReset(now);
            }

            // Daily limit check (shared across all endpoints)
            if (api.getRequestCount() >= DAILY_LIMIT) {
                sendJsonError(response, 429, "rate_limit_exceeded",
                        "Daily limit of " + DAILY_LIMIT + " requests reached. Try again tomorrow or upgrade your plan.");
                log.info("DAILY LIMIT HIT: key={} endpoint={} count={}/{}", apiKey, path, api.getRequestCount(), DAILY_LIMIT);
                return;
            }

            // Per-minute limit check — split by endpoint
            if (path.startsWith("/fact")) {
                if (api.getFactRequestCount() >= FACT_PER_MIN) {
                    sendJsonError(response, 429, "rate_limit_exceeded",
                            "Fact API limit of " + FACT_PER_MIN + " requests/min exceeded. Please wait.");
                    log.info("FACT RATE LIMIT HIT: key={} count={}/{}", apiKey, api.getFactRequestCount(), FACT_PER_MIN);
                    return;
                }
                api.setFactRequestCount(api.getFactRequestCount() + 1);

            } else if (path.startsWith("/generate-qr")) {
                if (api.getQrRequestCount() >= QR_PER_MIN) {
                    sendJsonError(response, 429, "rate_limit_exceeded",
                            "QR API limit of " + QR_PER_MIN + " requests/min exceeded. Please wait.");
                    log.info("QR RATE LIMIT HIT: key={} count={}/{}", apiKey, api.getQrRequestCount(), QR_PER_MIN);
                    return;
                }
                api.setQrRequestCount(api.getQrRequestCount() + 1);
            }

            // Increment daily counter and persist
            api.setRequestCount(api.getRequestCount() + 1);
            apiRepository.save(api);

            log.info("API KEY USED: key={} endpoint={} daily={}/{} fact={}/{} qr={}/{}",
                    apiKey, path,
                    api.getRequestCount(), DAILY_LIMIT,
                    api.getFactRequestCount(), FACT_PER_MIN,
                    api.getQrRequestCount(), QR_PER_MIN);
        }

        filterChain.doFilter(request, response);
    }

    private void sendJsonError(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\":\"%s\",\"message\":\"%s\"}", error, message));
    }
}