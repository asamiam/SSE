DROP DATABASE IF EXISTS `SSE`;

CREATE DATABASE `SSE`;

USE SSE;

DROP TABLE IF EXISTS `Certificates`;
DROP TABLE IF EXISTS `IPAddresses`;
DROP TABLE IF EXISTS `DomainFiles`;
DROP TABLE IF EXISTS `DomainDetails`;

CREATE TABLE `DomainDetails` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `domainName` VARCHAR(255) NOT NULL,
  `isSSLVerified` TINYINT(1) UNSIGNED,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`domainName`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `DomainFiles` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `domainID` BIGINT(20) UNSIGNED NOT NULL,
  `domainFile` VARCHAR(255) NOT NULL,
  `isPhishing` TINYINT(1) UNSIGNED DEFAULT NULL,
  `isThirdPartyPhishing` TINYINT(1) UNSIGNED DEFAULT NULL,
  `pageSource` LONGTEXT NOT NULL,
  `originalTimeStamp` DATETIME NOT NULL,
  `lastCrawledTimeStamp` DATETIME NOT NULL,
  `lastPhishedTimeStamp` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`domainID`,`domainFile`),
  CONSTRAINT `domainfiles_ibfk_1` FOREIGN KEY (`domainID`) REFERENCES `DomainDetails` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `IPAddresses` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `domainID` BIGINT(20) UNSIGNED NOT NULL,
  `ipAddress` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`domainID`,`ipAddress`),
  CONSTRAINT `ipaddresses_ibfk_1` FOREIGN KEY (`domainID`) REFERENCES `DomainDetails` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `Certificates` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `domainID` BIGINT(20) UNSIGNED NOT NULL,
  `issuerMD5` CHAR(32) CHARACTER SET ASCII COLLATE ascii_bin NOT NULL,
  `subjectMD5` CHAR(32) CHARACTER SET ASCII COLLATE ascii_bin NOT NULL,
  `issuerText` VARCHAR(255) NOT NULL,
  `subjectText` VARCHAR(255) NOT NULL,
  `validFrom` DATETIME NOT NULL,
  `validTill` DATETIME NOT NULL,
  `basicConstraint` TINYINT(4) UNSIGNED NOT NULL,
  `hierarchy` TINYINT(4) UNSIGNED NOT NULL,
  `fullCertificate` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`domainID`,`issuerMD5`,`subjectMD5`),
  CONSTRAINT `certificates_ibfk_1` FOREIGN KEY (`domainID`) REFERENCES `DomainDetails` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertDomainDetail//
CREATE PROCEDURE InsertDomainDetail(IN dName VARCHAR(255), OUT dID BIGINT)
BEGIN
INSERT INTO DomainDetails (domainName) VALUES (dName) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id);
SELECT Last_Insert_ID() INTO dID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertDomainFile//
CREATE PROCEDURE InsertDomainFile(IN dID BIGINT, IN dFile VARCHAR(255), IN pSource LONGTEXT)
BEGIN
INSERT INTO DomainFiles (domainID, domainFile, pageSource, originalTimeStamp, lastCrawledTimeStamp) VALUES (dID, dFile, pSource, Now(), Now()) ON DUPLICATE KEY UPDATE pageSource=pSource, lastCrawledTimeStamp=Now();
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertIPAddress//
CREATE PROCEDURE InsertIPAddress(IN dID BIGINT, IN ipAddy VARCHAR(15))
BEGIN
INSERT IGNORE INTO IPAddresses (domainID,ipAddress) VALUES (dID,INET_ATON(ipAddy));
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertCertificate//
CREATE PROCEDURE InsertCertificate(IN dID BIGINT(20),IN iText VARCHAR(255),IN sText VARCHAR(255),IN vFrom DATETIME,IN vTill DATETIME,IN bConstraint TINYINT(4),IN hier TINYINT(4),IN fCert TEXT)
BEGIN
INSERT INTO Certificates(domainID, issuerMD5, subjectMD5, issuerText, subjectText, validFrom, validTill, basicConstraint, hierarchy, fullCertificate) VALUES (dID,MD5(iText),MD5(sText),iText,sText,vFrom,vTill,bConstraint,hier,fCert) ON DUPLICATE KEY UPDATE validFrom=vFrom,validTill=vTill,basicConstraint=bConstraint,hierarchy=hier,fullCertificate=fCert;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS StoreSSLVerifiedResults//
CREATE PROCEDURE StoreSSLVerifiedResults(IN domainID BIGINT,IN result TINYINT(1))
BEGIN
UPDATE DomainDetails SET isSSLVerified = result WHERE id = domainID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS StorePhishingResults//
CREATE PROCEDURE StorePhishingResults(IN domainFileID BIGINT,IN result TINYINT(1))
BEGIN
UPDATE DomainFiles SET isPhishing = result,lastPhishedTimeStamp = Now() WHERE id = domainFileID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS StoreThirdPartyPhishingResults//
CREATE PROCEDURE StoreThirdPartyPhishingResults(IN domainFileID BIGINT,IN result TINYINT(1))
BEGIN
UPDATE DomainFiles SET isThirdPartyPhishing = result,lastPhishedTimeStamp = Now() WHERE id = domainFileID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS CheckIsPhishing//
CREATE PROCEDURE CheckIsPhishing(IN domainName VARCHAR(255), IN domainFile VARCHAR(255))
BEGIN
SELECT DomainFiles.isPhishing,DomainFiles.isThirdPartyPhishing FROM DomainDetails LEFT JOIN DomainFiles ON DomainDetails.id = DomainFiles.domainID WHERE DomainDetails.domainName = domainName AND DomainFiles.domainFile = domainFile;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS CheckIsSSLVerified//
CREATE PROCEDURE CheckIsSSLVerified(IN domainName VARCHAR(255))
BEGIN
SELECT DomainDetails.isSSLVerified FROM DomainDetails WHERE DomainDetails.domainName = domainName;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS GetCertificates//
CREATE PROCEDURE GetCertificates(IN domainID BIGINT)
BEGIN
SELECT id,domainID,issuerText,subjectText,validFrom,validTill,basicConstraint,hierarchy,fullCertificate FROM Certificates WHERE Certificates.domainID = domainID ORDER BY hierarchy;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS GetIPAddresses//
CREATE PROCEDURE GetIPAddresses(IN domainID BIGINT)
BEGIN
SELECT id,domainID,INET_NTOA(ipAddress) AS ipAddress FROM IPAddresses WHERE IPAddresses.domainID = domainID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS GetDomainDetail//
CREATE PROCEDURE GetDomainDetail(IN domainName VARCHAR(255))
BEGIN
SELECT id,domainName,isSSLVerified FROM DomainDetails WHERE DomainDetails.domainName = domainName;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS GetDomainFiles//
CREATE PROCEDURE GetDomainFiles(IN domainID BIGINT)
BEGIN
SELECT * FROM DomainFiles WHERE DomainFiles.domainID = domainID;
END;
//
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS GetDomainFile//
CREATE PROCEDURE GetDomainFile(IN domainName VARCHAR(255),IN domainFile VARCHAR(255))
BEGIN
SELECT DomainFiles.* FROM DomainDetails LEFT JOIN DomainFiles ON DomainDetails.id = DomainFiles.domainID WHERE DomainDetails.domainName = domainName AND DomainFiles.domainFile = domainFile;
END;
//
DELIMITER ;

/*Test data inserts!!*/
/*www.cnn.com*/

INSERT INTO DomainDetails VALUES (NULL, 'www.cnn.com', 1);

INSERT INTO Certificates VALUES (NULL, 1, MD5('www.verisign.com'), MD5('www.cnn.com'), 'www.verisign.com', 'www.cnn.com', '2009-01-01', '2012-01-01', 1, 1, 'fullCertText');

INSERT INTO DomainFiles VALUES (NULL, 1, 'index.html', 0, 0, '<html><body>Hello world</body></html>', '2010-01-27', '2010-01-27', '2010-01-27');

INSERT INTO IPAddresses VALUES (NULL, 1, '192.168.1.1');


/*www.google.com*/

INSERT INTO DomainDetails VALUES (NULL, 'www.google.com', 1);

INSERT INTO Certificates VALUES (NULL, 2, MD5('www.verisign.com'), MD5('www.google.com'), 'www.verisign.com', 'www.google.com', '2009-02-01', '2012-02-01', 1, 1, 'fullCertText');

INSERT INTO DomainFiles VALUES (NULL, 2, 'index.html', 0, 0, '<html><body>Hello world</body></html>', '2010-01-27', '2010-01-27', '2010-01-27');

INSERT INTO IPAddresses VALUES (NULL, 2, '192.168.1.2');

/*www.gamespot.com*/

INSERT INTO DomainDetails VALUES (NULL, 'www.gamespot.com', 1);

INSERT INTO Certificates VALUES (NULL, 3, MD5('www.verisign.com'), MD5('www.gamespot.com'), 'www.verisign.com', 'www.gamespot.com', '2009-03-01', '2012-03-01', 1, 1, 'fullCertText');

INSERT INTO DomainFiles VALUES (NULL, 3, 'index.html', 0, 0, '<html><body>Hello world</body></html>', '2010-01-27', '2010-01-27', '2010-01-27');

INSERT INTO IPAddresses VALUES (NULL, 3, '192.168.1.3');
