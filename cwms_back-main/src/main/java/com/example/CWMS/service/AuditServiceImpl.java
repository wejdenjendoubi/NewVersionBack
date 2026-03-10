package com.example.CWMS.service;

import com.example.CWMS.iservice.AuditService;
import com.example.CWMS.model.AuditLog;
import com.example.CWMS.model.AuditLog.EventType;
import com.example.CWMS.model.AuditLog.Severity;
import com.example.CWMS.model.User;
import com.example.CWMS.repository.AuditLogRepository;
import com.example.CWMS.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository     userRepository;
    private final ObjectMapper       objectMapper;

    // ── CONNEXIONS ────────────────────────────────────────────

    @Override
    public void logLogin(String username, String ip, String userAgent,
                         boolean success, String sessionId) {
        User user = userRepository.findByUsername(username).orElse(null);

        save(AuditLog.builder()
                .eventType (success ? EventType.LOGIN : EventType.LOGIN_FAILED)
                .severity  (success ? Severity.INFO   : Severity.WARNING)
                .action    (success ? "CONNEXION_OK"  : "TENTATIVE_ECHOUEE")
                .user      (user)
                .username  (username)
                .ipAddress (ip)
                .userAgent (userAgent)
                .sessionId (sessionId)
                .statusCode(success ? 200 : 401)
                .build());
    }

    @Override
    public void logLogout(String username, String ip, String sessionId) {
        User user = userRepository.findByUsername(username).orElse(null);

        save(AuditLog.builder()
                .eventType(EventType.LOGOUT)
                .severity (Severity.INFO)
                .action   ("DECONNEXION")
                .user     (user)
                .username (username)
                .ipAddress(ip)
                .sessionId(sessionId)
                .build());
    }

    // ── ACTIONS CRITIQUES ─────────────────────────────────────

    @Override
    public void logAction(String action, String entityType, String entityId,
                          Object oldObj, Object newObj) {
        try {
            AuditLog.AuditLogBuilder builder = AuditLog.builder()
                    .eventType    (resolveEventType(action))
                    .severity     (Severity.INFO)
                    .action       (action)
                    .entityType   (entityType)
                    .entityId     (entityId)
                    .oldValue     (toJson(oldObj))
                    .newValue     (toJson(newObj))
                    .correlationId(UUID.randomUUID().toString());

            enrichWithCurrentUser(builder);
            save(builder.build());

        } catch (Exception e) {
            log.error("Erreur log action: {}", e.getMessage());
        }
    }

    // ── ERREURS ───────────────────────────────────────────────

    @Override
    public void logError(Exception ex, HttpServletRequest request, int statusCode) {
        AuditLog.AuditLogBuilder builder = AuditLog.builder()
                .eventType   (EventType.ERROR)
                .severity    (statusCode >= 500 ? Severity.CRITICAL : Severity.ERROR)
                .ipAddress   (extractClientIp(request))
                .httpMethod  (request.getMethod())
                .endpoint    (request.getRequestURI())
                .statusCode  (statusCode)
                .errorMessage(ex.getMessage())
                .stackTrace  (truncateStackTrace(ex));

        enrichWithCurrentUser(builder);
        save(builder.build());
    }

    @Override
    public void logHttpError(HttpServletRequest request, int statusCode, long durationMs) {
        AuditLog.AuditLogBuilder builder = AuditLog.builder()
                .eventType (EventType.ERROR)
                .severity  (statusCode >= 500 ? Severity.CRITICAL : Severity.ERROR)
                .ipAddress (extractClientIp(request))
                .httpMethod(request.getMethod())
                .endpoint  (request.getRequestURI())
                .statusCode(statusCode)
                .durationMs(durationMs);

        enrichWithCurrentUser(builder);
        save(builder.build());
    }

    // ── UTILITAIRES ───────────────────────────────────────────

    @Override
    public void enrichWithCurrentUser(AuditLog.AuditLogBuilder builder) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            builder.username(username);
            userRepository.findByUsername(username).ifPresent(builder::user);
        }
    }

    @Override
    public void save(AuditLog auditLog) {
        try {
            AuditLog saved = auditLogRepository.save(auditLog);
            log.info("✅ Audit — id={} type={} user={}",
                    saved.getId(), saved.getEventType(), saved.getUsername());
        } catch (Exception e) {
            log.error("❌ Erreur sauvegarde audit: {}", e.getMessage(), e);
        }
    }

    @Override
    public String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }

    @Override
    public EventType resolveEventType(String action) {
        if (action == null) return EventType.READ;
        String a = action.toUpperCase();
        if (a.contains("CREATE") || a.contains("ADD")    || a.contains("SAVE"))    return EventType.CREATE;
        if (a.contains("UPDATE") || a.contains("EDIT")   || a.contains("MODIF"))   return EventType.UPDATE;
        if (a.contains("DELETE") || a.contains("REMOVE") || a.contains("SUPPRIM")) return EventType.DELETE;
        return EventType.READ;
    }

    @Override
    public String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        return xff != null ? xff.split(",")[0].trim() : request.getRemoteAddr();
    }

    @Override
    public String truncateStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String t = sw.toString();
        return t.length() > 4000 ? t.substring(0, 4000) + "..." : t;
    }
}