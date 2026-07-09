-- OboeToRe 初期設定SQL
-- インデックス設計方針:
--   各テーブルで user_id を軸に検索・ソートが走るため複合インデックスを定義
--   MySQLのFK列は自動でインデックスが作られるが、複合検索・ソートには追加定義が必要
-- MySQL 対象DB: oboetore (port: 3307)

CREATE DATABASE IF NOT EXISTS oboetore
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE oboetore;

-- =============================================
-- users テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS users (
    user_id         VARCHAR(255)    NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    nickname        VARCHAR(255),
    last_login_at   DATETIME,
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reminder_cache          TEXT,
    reminder_generated_at   DATETIME,
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- chat_histories テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS chat_histories (
    chat_id     BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     VARCHAR(255)    NOT NULL,
    role        VARCHAR(50)     NOT NULL,
    content     TEXT            NOT NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- tasks テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS tasks (
    task_id         BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         VARCHAR(255)    NOT NULL,
    title           VARCHAR(255)    NOT NULL,
    description     TEXT,
    priority        INT             NOT NULL COMMENT '1:低 2:中 3:高',
    start_date      DATE,
    start_time      TIME,
    due_date        DATE,
    due_time        TIME,
    location        VARCHAR(255),
    status          VARCHAR(50)     NOT NULL DEFAULT 'TODO',
    google_event_id VARCHAR(255),
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (task_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- shopping_list テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS shopping_list (
    shopping_id BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     VARCHAR(255)    NOT NULL,
    item_name   VARCHAR(255)    NOT NULL,
    description TEXT,
    status      VARCHAR(50)     NOT NULL DEFAULT 'TODO',
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (shopping_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- subsk テーブル
-- =============================================
CREATE TABLE IF NOT EXISTS subsk (
    subsk_id    BIGINT          NOT NULL AUTO_INCREMENT,
    subsk_name  VARCHAR(100)    NOT NULL,
    user_id     VARCHAR(255)    NOT NULL,
    joined_at   DATE            NOT NULL,
    update_at   DATE            NOT NULL,
    update_days  INT             NULL,
    PRIMARY KEY (subsk_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- インデックス
-- =============================================

-- chat_histories: user_id + created_at
--   findByUserId~OrderByCreatedAtAsc/Desc でソート込み検索が頻発するため
CREATE INDEX idx_chat_histories_user_created
    ON chat_histories (user_id, created_at);

-- tasks: user_id + status
--   findByUserIdAndStatus の複合検索
CREATE INDEX idx_tasks_user_status
    ON tasks (user_id, status);

-- tasks: due_date + status + user_id
--   findByDueDateAndStatusAndUserId（リマインダーで毎日実行される）
CREATE INDEX idx_tasks_due_status_user
    ON tasks (due_date, status, user_id);

-- tasks: user_id + google_event_id
--   findByUserIdAndGoogleEventId（Googleカレンダー連携）
CREATE INDEX idx_tasks_user_google_event
    ON tasks (user_id, google_event_id);

-- shopping_list: user_id + status
--   findByUserIdAndStatus の複合検索
CREATE INDEX idx_shopping_list_user_status
    ON shopping_list (user_id, status);

-- subsk: user_id + update_at
--   findByUserIdAndUpdateAt の複合検索
CREATE INDEX idx_subsk_user_update
    ON subsk (user_id, update_at);
