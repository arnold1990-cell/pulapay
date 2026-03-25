package com.pulapay.audit.service;

import com.pulapay.audit.entity.AuditLog;
import com.pulapay.audit.repository.AuditLogRepository;
import com.pulapay.user.entity.Role;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) { this.repository = repository; }

    public void log(String action, String actorPhone, Role role, String targetRef, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setActorPhoneNumber(actorPhone);
        log.setActorRole(role == null ? null : role.name());
        log.setTargetReference(targetRef);
        log.setDetails(details);
        repository.save(log);
    }
}
