package com.selina.lending.api.filter;

import com.selina.lending.config.properties.SlaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@Order(1)
public class SlaRequestFilter extends OncePerRequestFilter {

    private static final long NANO_SECONDS_IN_A_MILLI_SECOND = 1000000;

    private final SlaProperties slaProperties;

    public SlaRequestFilter(SlaProperties slaProperties) {
        this.slaProperties = slaProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var started = System.nanoTime();
            filterChain.doFilter(request, response);
            var elapsed = (System.nanoTime() - started) / NANO_SECONDS_IN_A_MILLI_SECOND;
            var requestSlaInfo = getRequestSlaInfo(request);

            if (requestSlaInfo.isPresent()) {
                var slaInfo = requestSlaInfo.get();
                if (elapsed > slaInfo.getTimeout()) {
                    log.warn("Lending API service slow HTTP response detected [name={}] [slaTimout={}] [elapsedTime={}]", slaInfo.getName(), slaInfo.getTimeout(), elapsed);
                }
            }
        } catch (Exception ex) {
            log.error("Error while measuring the request's SLA", ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        try {
            return !hasSlaProperties() || !isTracked(request);
        } catch (Exception ex) {
            log.error("Error while checking if the request's SLA should be measured", ex);
            return true;
        }
    }

    private boolean hasSlaProperties() {
        return slaProperties != null && slaProperties.getRequests() != null;
    }

    private boolean isTracked(HttpServletRequest request) {
        return slaProperties.getRequests().stream()
                .anyMatch(slaRequestInfo -> matchesRequest(request, slaRequestInfo));
    }

    private Optional<SlaProperties.Request> getRequestSlaInfo(HttpServletRequest request) {
        return slaProperties.getRequests().stream()
                .filter(slaRequestInfo -> matchesRequest(request, slaRequestInfo))
                .findFirst();
    }

    private static boolean matchesRequest(HttpServletRequest request, SlaProperties.Request slaRequestInfo) {
        return slaRequestInfo.getHttpMethod().equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().matches(slaRequestInfo.getUrlPattern());
    }
}
