-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 12, 2020 at 03:44 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `doanstp`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `Id` int(10) NOT NULL,
  `Username` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`Id`, `Username`, `Password`) VALUES
(1, 'lingardinho', '123'),
(2, 'cristiano7', '123'),
(3, 'kepa', '123'),
(5, 'auba', '123'),
(6, 'lacazette', '123'),
(7, 'ozil', '12');

-- --------------------------------------------------------

--
-- Table structure for table `application`
--

CREATE TABLE `application` (
  `id` int(11) NOT NULL,
  `playerid` int(11) DEFAULT NULL,
  `teamid` int(11) DEFAULT NULL,
  `isapprove` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `application`
--

INSERT INTO `application` (`id`, `playerid`, `teamid`, `isapprove`) VALUES
(15, 5, 3, 1),
(61, 6, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `draftedmatch`
--

CREATE TABLE `draftedmatch` (
  `Id` int(10) NOT NULL,
  `IsAccepted` bit(1) NOT NULL,
  `IsDenied` bit(1) NOT NULL,
  `Day` date NOT NULL,
  `Place` varchar(255) NOT NULL,
  `time` time NOT NULL,
  `isend` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `draftedmatch`
--

INSERT INTO `draftedmatch` (`Id`, `IsAccepted`, `IsDenied`, `Day`, `Place`, `time`, `isend`) VALUES
(13, b'1', b'0', '2020-08-19', 'ad', '09:00:00', 1),
(14, b'0', b'0', '2020-08-19', 'ad', '16:00:00', 0),
(15, b'0', b'0', '2020-08-23', 'phuc xa', '10:05:00', 0),
(17, b'0', b'1', '2020-08-27', 'Thanh Nhan', '06:00:00', 0),
(19, b'1', b'0', '2020-08-28', 'PX', '08:00:00', 1),
(21, b'0', b'0', '2020-08-19', 'ad', '16:00:00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `draftedmatch_team`
--

CREATE TABLE `draftedmatch_team` (
  `id` int(11) NOT NULL,
  `draftedmatchid` int(11) NOT NULL,
  `teamid` int(11) NOT NULL,
  `role` varchar(10) NOT NULL,
  `isready` tinyint(1) NOT NULL,
  `resstt` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `draftedmatch_team`
--

INSERT INTO `draftedmatch_team` (`id`, `draftedmatchid`, `teamid`, `role`, `isready`, `resstt`) VALUES
(8, 13, 3, 'create', 1, 1),
(9, 14, 3, 'create', 1, 0),
(10, 15, 2, 'create', 1, 0),
(11, 13, 1, 'guest', 0, 0),
(12, 14, 1, 'guest', 0, 0),
(13, 13, 2, 'guest', 1, 3),
(16, 17, 2, 'invite', 1, 0),
(17, 17, 3, 'invited', 0, 0),
(20, 19, 3, 'invite', 1, 2),
(21, 19, 1, 'invited', 1, 2),
(24, 21, 2, 'invite', 1, 0),
(25, 21, 1, 'invited', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `Id` int(10) NOT NULL,
  `TeamId` int(10) DEFAULT NULL,
  `AccountId` int(10) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `BirthYear` int(10) NOT NULL,
  `Position` varchar(255) DEFAULT NULL,
  `IsCaptain` bit(1) NOT NULL,
  `Icon` varchar(255) DEFAULT NULL,
  `phone` varchar(25) NOT NULL,
  `regid` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `player`
--

INSERT INTO `player` (`Id`, `TeamId`, `AccountId`, `Name`, `BirthYear`, `Position`, `IsCaptain`, `Icon`, `phone`, `regid`) VALUES
(1, 1, 1, 'Linh ga', 1993, 'FW', b'1', '', '123456', 'epxk01fsSQiZKlg_g2Byld:APA91bG3Oh8fZRgMHlvnjy3KYwd3M0-SwRqRDTQZ_mtOmzOUSf1fo2M_dMgpo6dUJQS0FvmP_w-HW_nnSZsK5JEY_H-vvVKrTKsCNW_rZ50Jw5hhKVfavUWyNPsgq0Sv22JohT2kJLD6'),
(2, 1, 2, 'Ronaldo', 1986, 'MF', b'0', '', '123456', 'epxk01fsSQiZKlg_g2Byld:APA91bG3Oh8fZRgMHlvnjy3KYwd3M0-SwRqRDTQZ_mtOmzOUSf1fo2M_dMgpo6dUJQS0FvmP_w-HW_nnSZsK5JEY_H-vvVKrTKsCNW_rZ50Jw5hhKVfavUWyNPsgq0Sv22JohT2kJLD6'),
(3, 2, 3, 'Arizabalaga Kepa', 1988, 'GK', b'1', '', '123456', 'epxk01fsSQiZKlg_g2Byld:APA91bG3Oh8fZRgMHlvnjy3KYwd3M0-SwRqRDTQZ_mtOmzOUSf1fo2M_dMgpo6dUJQS0FvmP_w-HW_nnSZsK5JEY_H-vvVKrTKsCNW_rZ50Jw5hhKVfavUWyNPsgq0Sv22JohT2kJLD6'),
(4, 3, 5, 'Aubameyang', 1988, 'FW', b'1', '', '123456', 'dap845i9QjqCrBUUQfG8uR:APA91bGCuzfCbWtVAyNzLvGmRkqBRSngT7fz5r6WuUj6Vc8gB0AoU8NpulXAFii-SbChm1V5E6D1XMoPv0ye3tVQM7SFDsFfur9dAGjsegHtatDZuP-_TEGf_3WL8YgyMKB7ldy_xkzr'),
(5, 3, 6, 'Alexandre Lacazette', 1988, 'FW', b'0', '', '1234456', 'epxk01fsSQiZKlg_g2Byld:APA91bG3Oh8fZRgMHlvnjy3KYwd3M0-SwRqRDTQZ_mtOmzOUSf1fo2M_dMgpo6dUJQS0FvmP_w-HW_nnSZsK5JEY_H-vvVKrTKsCNW_rZ50Jw5hhKVfavUWyNPsgq0Sv22JohT2kJLD6'),
(6, 3, 7, 'Ozil', 1987, 'MF', b'0', '', '123456', 'epxk01fsSQiZKlg_g2Byld:APA91bG3Oh8fZRgMHlvnjy3KYwd3M0-SwRqRDTQZ_mtOmzOUSf1fo2M_dMgpo6dUJQS0FvmP_w-HW_nnSZsK5JEY_H-vvVKrTKsCNW_rZ50Jw5hhKVfavUWyNPsgq0Sv22JohT2kJLD6');

-- --------------------------------------------------------

--
-- Table structure for table `playerstatistic`
--

CREATE TABLE `playerstatistic` (
  `Id` int(10) NOT NULL,
  `PlayerId` int(10) NOT NULL,
  `Goals` int(10) NOT NULL,
  `Tackle` int(10) NOT NULL,
  `Block` int(10) NOT NULL,
  `Assist` int(10) NOT NULL,
  `Save` int(10) NOT NULL,
  `Penalty` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `playerstatistic`
--

INSERT INTO `playerstatistic` (`Id`, `PlayerId`, `Goals`, `Tackle`, `Block`, `Assist`, `Save`, `Penalty`) VALUES
(1, 1, 1, 0, 0, 0, 0, 0),
(2, 2, 0, 0, 0, 0, 0, 0),
(3, 3, 0, 0, 0, 0, 0, 0),
(4, 4, 0, 0, 0, 0, 0, 0),
(5, 5, 1, 0, 0, 0, 0, 0),
(6, 6, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `scoretable`
--

CREATE TABLE `scoretable` (
  `Id` int(11) NOT NULL,
  `DraftedMatchId` int(11) NOT NULL,
  `FinalScore` text NOT NULL,
  `Statistic` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `scoretable`
--

INSERT INTO `scoretable` (`Id`, `DraftedMatchId`, `FinalScore`, `Statistic`) VALUES
(9, 13, '0-4', 'a'),
(10, 19, '2-2', 'a');

-- --------------------------------------------------------

--
-- Table structure for table `team`
--

CREATE TABLE `team` (
  `Id` int(10) NOT NULL,
  `TeamLocationId` int(10) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Rating` double NOT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `Icon` varchar(255) DEFAULT NULL,
  `win` int(11) DEFAULT NULL,
  `draw` int(11) DEFAULT NULL,
  `lose` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team`
--

INSERT INTO `team` (`Id`, `TeamLocationId`, `Name`, `Rating`, `phone`, `Icon`, `win`, `draw`, `lose`) VALUES
(1, 1, 'MU', 1, '123456', '', 0, 0, 0),
(2, 2, 'Chelsea', 1, '123456', '', 0, 0, 0),
(3, 3, 'Arsenal', 1, '123456', '', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `teamlocation`
--

CREATE TABLE `teamlocation` (
  `Id` int(10) NOT NULL,
  `Street` varchar(255) DEFAULT NULL,
  `District` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `Latitude` double NOT NULL,
  `Longtitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teamlocation`
--

INSERT INTO `teamlocation` (`Id`, `Street`, `District`, `City`, `Latitude`, `Longtitude`) VALUES
(1, 'km 10 tran phu', 'ha dong', 'ha noi', 20.981211, 105.787136),
(2, 'km 10 tran phu', 'hoan kiem', 'ha noi', 20.981211, 105.787136),
(3, 'km 10 tran phu', 'ha dong', 'ha noi', 20.980073, 105.789641);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `application`
--
ALTER TABLE `application`
  ADD PRIMARY KEY (`id`),
  ADD KEY `playerid` (`playerid`),
  ADD KEY `teamid` (`teamid`);

--
-- Indexes for table `draftedmatch`
--
ALTER TABLE `draftedmatch`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `draftedmatch_team`
--
ALTER TABLE `draftedmatch_team`
  ADD PRIMARY KEY (`id`),
  ADD KEY `draftedmatchid` (`draftedmatchid`),
  ADD KEY `teamid` (`teamid`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `FKPlayer612164` (`AccountId`),
  ADD KEY `FKPlayer841034` (`TeamId`);

--
-- Indexes for table `playerstatistic`
--
ALTER TABLE `playerstatistic`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `FKPlayerStat300404` (`PlayerId`);

--
-- Indexes for table `scoretable`
--
ALTER TABLE `scoretable`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `DraftedMatchId` (`DraftedMatchId`);

--
-- Indexes for table `team`
--
ALTER TABLE `team`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `FKTeam34781` (`TeamLocationId`);

--
-- Indexes for table `teamlocation`
--
ALTER TABLE `teamlocation`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `application`
--
ALTER TABLE `application`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `draftedmatch`
--
ALTER TABLE `draftedmatch`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `draftedmatch_team`
--
ALTER TABLE `draftedmatch_team`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `playerstatistic`
--
ALTER TABLE `playerstatistic`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `scoretable`
--
ALTER TABLE `scoretable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `team`
--
ALTER TABLE `team`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `teamlocation`
--
ALTER TABLE `teamlocation`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `application`
--
ALTER TABLE `application`
  ADD CONSTRAINT `application_ibfk_1` FOREIGN KEY (`playerid`) REFERENCES `player` (`Id`),
  ADD CONSTRAINT `application_ibfk_2` FOREIGN KEY (`teamid`) REFERENCES `team` (`Id`);

--
-- Constraints for table `draftedmatch_team`
--
ALTER TABLE `draftedmatch_team`
  ADD CONSTRAINT `draftedmatch_team_ibfk_1` FOREIGN KEY (`draftedmatchid`) REFERENCES `draftedmatch` (`Id`),
  ADD CONSTRAINT `draftedmatch_team_ibfk_2` FOREIGN KEY (`teamid`) REFERENCES `team` (`Id`);

--
-- Constraints for table `player`
--
ALTER TABLE `player`
  ADD CONSTRAINT `FKPlayer612164` FOREIGN KEY (`AccountId`) REFERENCES `account` (`Id`),
  ADD CONSTRAINT `FKPlayer841034` FOREIGN KEY (`TeamId`) REFERENCES `team` (`Id`);

--
-- Constraints for table `playerstatistic`
--
ALTER TABLE `playerstatistic`
  ADD CONSTRAINT `FKPlayerStat300404` FOREIGN KEY (`PlayerId`) REFERENCES `player` (`Id`);

--
-- Constraints for table `scoretable`
--
ALTER TABLE `scoretable`
  ADD CONSTRAINT `scoretable_ibfk_1` FOREIGN KEY (`DraftedMatchId`) REFERENCES `draftedmatch` (`Id`);

--
-- Constraints for table `team`
--
ALTER TABLE `team`
  ADD CONSTRAINT `FKTeam34781` FOREIGN KEY (`TeamLocationId`) REFERENCES `teamlocation` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
