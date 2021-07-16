CREATE SCHEMA `todomanagementdb` ;

CREATE TABLE `todomanagementdb`.`todotb` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `workname` VARCHAR(100) NULL,
  `startingdate` DATE NULL,
  `endingdate` DATE NULL,
  `status` VARCHAR(10) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;