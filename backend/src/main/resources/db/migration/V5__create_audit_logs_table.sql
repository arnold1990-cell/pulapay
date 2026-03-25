CREATE TABLE audit_logs (
  id BIGSERIAL PRIMARY KEY,
  action VARCHAR(100) NOT NULL,
  actor_phone_number VARCHAR(20),
  actor_role VARCHAR(20),
  target_reference VARCHAR(255),
  details VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_audit_action ON audit_logs(action);
