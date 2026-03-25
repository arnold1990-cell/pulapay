package com.pulapay.audit.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String action;
    private String actorPhoneNumber;
    private String actorRole;
    private String targetReference;
    @Column(nullable = false, length = 1000)
    private String details;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist void prePersist(){ createdAt = Instant.now(); }

    public void setAction(String action) { this.action = action; }
    public void setActorPhoneNumber(String actorPhoneNumber) { this.actorPhoneNumber = actorPhoneNumber; }
    public void setActorRole(String actorRole) { this.actorRole = actorRole; }
    public void setTargetReference(String targetReference) { this.targetReference = targetReference; }
    public void setDetails(String details) { this.details = details; }
}
