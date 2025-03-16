CREATE TABLE IF NOT EXISTS `TRADES` (
  `trade_id` VARCHAR(255)  PRIMARY KEY,
  `version` INT NOT NULL,
  `counter_party_id` VARCHAR(255) NOT NULL,
  `book_id` VARCHAR(255) NOT NULL,
  `maturity_date` DATE NOT NULL,
  `created_date` DATE NOT NULL,
  `expired` BOOLEAN DEFAULT FALSE
  );

CREATE INDEX idx_maturity_date ON TRADES(maturity_date);
CREATE INDEX idx_counter_party ON TRADES(counter_party_id);

