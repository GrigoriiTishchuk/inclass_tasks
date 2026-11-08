CREATE DATABASE IF NOT EXISTS temperature_converter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE temperature_converter;


CREATE TABLE IF NOT EXISTS temperature_unit (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                name VARCHAR(50) NOT NULL,
                                                symbol VARCHAR(10) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS unit_translation (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                unit_id INT NOT NULL,
                                                lang VARCHAR(10) NOT NULL,
                                                name VARCHAR(50) NOT NULL,
                                                description VARCHAR(255),
                                                FOREIGN KEY (unit_id) REFERENCES temperature_unit(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS temperature_conversion (
                                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                                      source_unit_id INT NOT NULL,
                                                      target_unit_id INT NOT NULL,
                                                      original_value DOUBLE NOT NULL,
                                                      converted_value DOUBLE NOT NULL,
                                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                      FOREIGN KEY (source_unit_id) REFERENCES temperature_unit(id),
                                                      FOREIGN KEY (target_unit_id) REFERENCES temperature_unit(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT IGNORE INTO temperature_unit(id, name, symbol) VALUES
                                                          (1, 'Celsius', 'C'),
                                                          (2, 'Fahrenheit', 'F'),
                                                          (3, 'Kelvin', 'K');

INSERT IGNORE INTO unit_translation(unit_id, lang, name, description) VALUES
-- Celsius (id=1)
(1, 'en', 'Celsius', 'Metric temperature scale'),
(1, 'ar', 'مئوية', 'مقياس درجات الحرارة المترية'),
(1, 'fi', 'Celsius', 'Metrinen lämpötila-asteikko'),
(1, 'sv', 'Celsius', 'Metrisk temperaturskala'),
(1, 'ja', '摂氏', '摂氏温度目盛'),

-- Fahrenheit (id=2)
(2, 'en', 'Fahrenheit', 'Imperial temperature scale'),
(2, 'ar', 'فهرنهايت', 'مقياس حرارة إمبراطوري'),
(2, 'fi', 'Fahrenheit', 'Fahrenheit-asteikko'),
(2, 'sv', 'Fahrenheit', 'Fahrenheit-skala'),
(2, 'ja', '華氏', '華氏温度目盛'),

-- Kelvin (id=3)
(3, 'en', 'Kelvin', 'Absolute temperature scale'),
(3, 'ar', 'كلفن', 'مقياس الحرارة المطلق'),
(3, 'fi', 'Kelvin', 'Absoluuttinen lämpötila-asteikko'),
(3, 'sv', 'Kelvin', 'Absolut temperaturskala'),
(3, 'ja', 'ケルビン', '絶対温度目盛');;