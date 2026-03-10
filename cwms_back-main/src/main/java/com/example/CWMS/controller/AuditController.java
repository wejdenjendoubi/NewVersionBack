package com.example.CWMS.controller;

import com.example.CWMS.dto.ApiResponse;
import com.example.CWMS.dto.AuditLogDTO;
import com.example.CWMS.model.AuditLog.EventType;
import com.example.CWMS.model.AuditLog.Severity;
import com.example.CWMS.model.User;
import com.example.CWMS.repository.AuditLogRepository;
import com.example.CWMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit") // ✅ URL corrigée
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository     userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuditLogDTO>>> search(
            @RequestParam(required = false) String    eventType,
            @RequestParam(required = false) String    severity,
            @RequestParam(required = false) Integer   userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable) {

        EventType et  = (eventType != null && !eventType.isEmpty())
                ? EventType.valueOf(eventType) : null;
        Severity  sev = (severity  != null && !severity.isEmpty())
                ? Severity.valueOf(severity)   : null;

        return ResponseEntity.ok(ApiResponse.success(
                auditLogRepository.search(et, sev, userId, from, to, pageable)
                        .map(AuditLogDTO::from)
        ));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<AuditLogDTO>>> getByUser(
            @PathVariable Integer userId, Pageable pageable) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + userId));

        return ResponseEntity.ok(ApiResponse.success(
                auditLogRepository.findByUser(user, pageable).map(AuditLogDTO::from)
        ));
    }

    @GetMapping("/user/{userId}/connections")
    public ResponseEntity<ApiResponse<List<AuditLogDTO>>> getConnections(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(ApiResponse.success(
                auditLogRepository.findConnectionsByUserId(userId)
                        .stream()
                        .map(AuditLogDTO::from)
                        .toList()
        ));
    }
}