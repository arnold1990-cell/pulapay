package com.pulapay.user.service;

import com.pulapay.audit.service.AuditLogService;
import com.pulapay.common.exception.BadRequestException;
import com.pulapay.user.dto.UpdateProfileRequest;
import com.pulapay.user.dto.UserResponse;
import com.pulapay.user.entity.User;
import com.pulapay.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public UserService(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    public UserResponse getMe(User user) { return toResponse(user); }

    public UserResponse updateProfile(User user, UpdateProfileRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(existing -> {
            if (!existing.getId().equals(user.getId())) throw new BadRequestException("Email already in use");
        });
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        User saved = userRepository.save(user);
        auditLogService.log("PROFILE_UPDATE", saved.getPhoneNumber(), saved.getRole(), saved.getId().toString(), "User profile updated");
        return toResponse(saved);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getPhoneNumber(), u.getEmail(), u.getRole(), u.isActive(), u.getCreatedAt(), u.getUpdatedAt());
    }
}
