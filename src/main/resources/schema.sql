-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS rehab_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE rehab_db;

-- 사용자 테이블
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    nickname VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(255),
    gender VARCHAR(20),
    profile_image_url VARCHAR(500),
    role VARCHAR(20),
    current_streak INT,
    max_streak INT,
    last_activity_date DATE,
    fcm_token VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 주소 테이블
CREATE TABLE address (
    address_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    city VARCHAR(255),
    district VARCHAR(255),
    street VARCHAR(255),
    zipcode VARCHAR(20),
    street_code VARCHAR(50),
    spec_address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 약관 테이블
CREATE TABLE terms (
    terms_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    is_required BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 약관 동의 테이블
CREATE TABLE consent (
    agree_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    terms_id BIGINT NOT NULL,
    is_agreed BOOLEAN NOT NULL,
    agreed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (terms_id) REFERENCES terms(terms_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_terms (user_id, terms_id)
);

-- 운동 테이블
CREATE TABLE exercise (
    exercise_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    body_part VARCHAR(100),
    difficulty VARCHAR(20),
    contraindications JSON,
    progression_rules JSON,
    evidence_level VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 운동 미디어 테이블
CREATE TABLE exercise_media (
    exercise_media_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercise_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    media_type VARCHAR(20),
    language VARCHAR(10),
    duration INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id) ON DELETE CASCADE
);

-- 운동 이미지 테이블
CREATE TABLE exercise_image (
    exercise_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercise_id BIGINT NOT NULL,
    title VARCHAR(255),
    image_url JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id) ON DELETE CASCADE
);

-- 의학 지식 테이블
CREATE TABLE medical_knowledge (
    knowledge_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    source VARCHAR(500),
    evidence_level VARCHAR(10),
    metadata JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 운동-의학지식 매핑 테이블
CREATE TABLE exercise_evidence (
    evidence_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exercise_id BIGINT NOT NULL,
    knowledge_id BIGINT NOT NULL,
    recommendation_basis TEXT,
    clinical_guidelines JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id) ON DELETE CASCADE,
    FOREIGN KEY (knowledge_id) REFERENCES medical_knowledge(knowledge_id) ON DELETE CASCADE
);

-- 재활 플랜 테이블
CREATE TABLE rehab_plan (
    rehab_plan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    status VARCHAR(20),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    meta JSON,
    generated_by VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 플랜 아이템 테이블
CREATE TABLE plan_item (
    plan_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rehab_plan_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    order_index INT,
    phase VARCHAR(20),
    dose JSON,
    status VARCHAR(20),
    recommendation_reason JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (rehab_plan_id) REFERENCES rehab_plan(rehab_plan_id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id) ON DELETE CASCADE
);

-- 운동 로그 테이블
CREATE TABLE exercise_log (
    exercise_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_item_id BIGINT NOT NULL,
    logged_at TIMESTAMP NOT NULL,
    pain_before INT,
    pain_after INT,
    rpe INT,
    completion_rate INT,
    duration_sec INT,
    notes TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (plan_item_id) REFERENCES plan_item(plan_item_id) ON DELETE CASCADE
);

-- 일일 요약 테이블
CREATE TABLE daily_summary (
    summary_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    all_exercises_completed BOOLEAN DEFAULT FALSE,
    exercise_completion_rate INT DEFAULT 0,
    all_medications_taken BOOLEAN DEFAULT FALSE,
    medication_completion_rate INT DEFAULT 0,
    avg_pain_score INT,
    total_duration_sec INT DEFAULT 0,
    daily_metrics JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, date)
);

-- 회복 점수 테이블
CREATE TABLE recovery_score (
    recovery_score_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    daily_score DECIMAL(5,2),
    score_trend_7d JSON,
    score_trend_14d JSON,
    score_factors JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date_recovery (user_id, date)
);

-- 복약 테이블
CREATE TABLE medication (
    medication_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    dose VARCHAR(100),
    route VARCHAR(50),
    instructions TEXT,
    description TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 복약 스케줄 테이블
CREATE TABLE medi_schedule (
    medi_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medication_id BIGINT NOT NULL,
    time_of_day VARCHAR(20) NOT NULL,
    notify BOOLEAN DEFAULT TRUE,
    rrule VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (medication_id) REFERENCES medication(medication_id) ON DELETE CASCADE
);

-- 복약 로그 테이블
CREATE TABLE medication_log (
    medication_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    medication_id BIGINT NOT NULL,
    taken_at TIMESTAMP NOT NULL,
    time_of_day VARCHAR(20),
    taken BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) REFERENCES medication(medication_id) ON DELETE CASCADE
);

-- 알림 테이블
CREATE TABLE reminder (
    reminder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    rule JSON NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    next_fire_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 리포트 스냅샷 테이블
CREATE TABLE report_snapshot (
    report_snapshot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    period VARCHAR(20) NOT NULL,
    covered_range JSON NOT NULL,
    metrics JSON,
    weekly_highlight JSON,
    recovery_prediction DECIMAL(5,2),
    generated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- AI 추론 로그 테이블
CREATE TABLE ai_inference_log (
    inference_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    model_key VARCHAR(100) NOT NULL,
    model_version VARCHAR(50),
    input_snapshot JSON,
    output_snapshot JSON,
    knowledge_references JSON,
    latency_ms INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 감사 로그 테이블
CREATE TABLE audit_log (
    audit_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity VARCHAR(100),
    details JSON,
    ip_address VARCHAR(50),
    trace_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_address_user_id ON address(user_id);
CREATE INDEX idx_consent_user_id ON consent(user_id);
CREATE INDEX idx_consent_terms_id ON consent(terms_id);
CREATE INDEX idx_exercise_media_exercise_id ON exercise_media(exercise_id);
CREATE INDEX idx_exercise_image_exercise_id ON exercise_image(exercise_id);
CREATE INDEX idx_exercise_evidence_exercise_id ON exercise_evidence(exercise_id);
CREATE INDEX idx_exercise_evidence_knowledge_id ON exercise_evidence(knowledge_id);
CREATE INDEX idx_rehab_plan_user_id ON rehab_plan(user_id);
CREATE INDEX idx_plan_item_rehab_plan_id ON plan_item(rehab_plan_id);
CREATE INDEX idx_plan_item_exercise_id ON plan_item(exercise_id);
CREATE INDEX idx_exercise_log_user_id ON exercise_log(user_id);
CREATE INDEX idx_exercise_log_plan_item_id ON exercise_log(plan_item_id);
CREATE INDEX idx_exercise_log_logged_at ON exercise_log(logged_at);
CREATE INDEX idx_daily_summary_user_id ON daily_summary(user_id);
CREATE INDEX idx_daily_summary_date ON daily_summary(date);
CREATE INDEX idx_recovery_score_user_id ON recovery_score(user_id);
CREATE INDEX idx_recovery_score_date ON recovery_score(date);
CREATE INDEX idx_medication_user_id ON medication(user_id);
CREATE INDEX idx_medi_schedule_medication_id ON medi_schedule(medication_id);
CREATE INDEX idx_medication_log_user_id ON medication_log(user_id);
CREATE INDEX idx_medication_log_medication_id ON medication_log(medication_id);
CREATE INDEX idx_medication_log_taken_at ON medication_log(taken_at);
CREATE INDEX idx_reminder_user_id ON reminder(user_id);
CREATE INDEX idx_reminder_next_fire_at ON reminder(next_fire_at);
CREATE INDEX idx_report_snapshot_user_id ON report_snapshot(user_id);
CREATE INDEX idx_ai_inference_log_user_id ON ai_inference_log(user_id);
CREATE INDEX idx_audit_log_user_id ON audit_log(user_id);
CREATE INDEX idx_audit_log_created_at ON audit_log(created_at);
