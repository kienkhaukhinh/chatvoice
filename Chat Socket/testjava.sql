-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 23, 2016 at 01:45 PM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `testjava`
--

-- --------------------------------------------------------

--
-- Table structure for table `conversation`
--

CREATE TABLE `conversation` (
  `ID` int(11) NOT NULL,
  `ID_user_1` int(11) NOT NULL,
  `ID_user_2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `conversation`
--

INSERT INTO `conversation` (`ID`, `ID_user_1`, `ID_user_2`) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 1, 3),
(4, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `info_user`
--

CREATE TABLE `info_user` (
  `ID` int(11) NOT NULL,
  `user_name` text COLLATE utf8_vietnamese_ci NOT NULL,
  `pass` text COLLATE utf8_vietnamese_ci NOT NULL,
  `fullname` text COLLATE utf8_vietnamese_ci,
  `status` text COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `info_user`
--

INSERT INTO `info_user` (`ID`, `user_name`, `pass`, `fullname`, `status`) VALUES
(1, 'a', '1', 'Dương Đình Tân', ''),
(2, 'b', '1', 'full name b', ''),
(3, 'c', '1', 'full name c', ''),
(4, 'd', '', 'full name d', ''),
(5, 'e', '1', 'full name e', ''),
(6, 'f', '1', 'full name f', ''),
(7, 'tantinhte01', '1', 'duong tan 01', ''),
(8, 'tantinhte02', '1', 'duong tan 02', ''),
(9, 'tantinhte03', '1', 'duong tan 03', ''),
(10, 'tantinhte01', '1', 'duong tan 01', ''),
(11, 'tantinhte02', '1', 'duong tan 02', ''),
(12, 'tantinhte03', '1', 'duong tan 03', ''),
(13, 'tantinhte01', '1', 'duong tan 01', ''),
(14, 'tantinhte02', '1', 'duong tan 02', ''),
(15, 'tantinhte03', '1', 'duong tan 03', ''),
(16, 'p', 'p', 'p', ''),
(17, 'z', '1', 'my name is z', '');

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `ID_user` int(11) NOT NULL,
  `msg` text COLLATE utf8_vietnamese_ci NOT NULL,
  `id_conversation` int(11) NOT NULL,
  `TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`ID_user`, `msg`, `id_conversation`, `TIME`) VALUES
(1, ' HDFX HDFX DF', 1, '2016-02-21 16:28:50'),
(1, ' TUF BTGDF GBNGCDT', 1, '2016-02-21 16:28:50'),
(1, '', 1, '2016-02-21 16:29:18'),
(1, 'jTextField1', 1, '2016-02-21 17:12:41'),
(2, 'jTextField1', 2, '2016-02-21 17:13:03'),
(2, 'asf', 2, '2016-02-21 17:13:07'),
(2, 'qwer', 2, '2016-02-21 17:13:15'),
(1, 'qwer', 1, '2016-02-21 17:13:17'),
(1, 'q rqwer', 1, '2016-02-21 17:13:19'),
(1, 'jTextField1', 1, '2016-02-21 17:14:36'),
(1, 'asd fasdf ', 1, '2016-02-21 17:14:39'),
(1, 'asd fasd fasd', 1, '2016-02-21 17:14:41'),
(1, 'asd fasdf asdfasd', 1, '2016-02-21 17:14:43'),
(2, 'jTextField1', 2, '2016-02-21 17:14:45'),
(2, 'hello', 2, '2016-02-21 17:14:49'),
(2, 'nghe ro kohng', 2, '2016-02-21 17:14:57'),
(1, 'ro vl ;))', 1, '2016-02-21 17:15:02'),
(1, 'jTextField1', 3, '2016-02-21 17:17:13'),
(1, 'efewef', 3, '2016-02-21 17:17:28'),
(1, 'sddfssdf', 3, '2016-02-21 17:17:30'),
(1, 'easdf a', 3, '2016-02-21 17:17:31'),
(1, 'asd fasdf', 3, '2016-02-21 17:17:32'),
(1, 'asdf asdf', 3, '2016-02-21 17:17:34'),
(1, 'asdf asdfasdfasd faasdf asdf', 3, '2016-02-21 17:17:37'),
(1, 'asdf aasfasdf asdf asdf', 3, '2016-02-21 17:17:40'),
(1, 'jTextField1', 1, '2016-02-21 17:24:46'),
(1, 'sdfg ', 1, '2016-02-21 17:24:52'),
(2, 'jTextField1asdf ', 2, '2016-02-21 17:24:54'),
(1, 'asdf ', 1, '2016-02-21 17:24:56'),
(1, '  asdf jaksdf asd fas asdfjasdnhfj  asdfj asdf asdfk asdkf  asdf jas asdf asdf a sdf  asdf a sdf  asdf   asd fasd fa sd fa sd f asd  f asd ', 1, '2016-02-21 17:25:18'),
(2, 'fasdf', 2, '2016-02-21 17:25:37'),
(2, 'asdf', 2, '2016-02-21 17:25:39'),
(2, 'asdf', 2, '2016-02-21 17:25:40'),
(2, 'asdf', 2, '2016-02-21 17:25:42'),
(1, 'jTextField1', 1, '2016-02-22 10:09:05'),
(2, 'jTextField1', 2, '2016-02-22 10:09:20'),
(1, 'h', 1, '2016-02-22 10:09:26'),
(1, 'dfgh', 1, '2016-02-22 10:09:29'),
(1, 'dfhdfhdfghdfgh', 1, '2016-02-22 10:09:33'),
(1, 'hello', 1, '2016-02-22 18:24:30'),
(2, 'jTextField1', 2, '2016-02-22 18:24:34'),
(2, 'toi day', 2, '2016-02-22 18:24:41'),
(1, 'dep trai von', 1, '2016-02-22 18:24:48'),
(2, 'l?i b?o x?u ?i', 2, '2016-02-22 18:24:58'),
(1, 'jTextField1', 1, '2016-02-22 18:28:44'),
(1, 'asd ', 1, '2016-02-22 18:28:46'),
(1, 'asd ádfasd', 1, '2016-02-22 18:28:49'),
(1, 'asdfasdasdf ', 1, '2016-02-22 18:28:51'),
(1, 'jTextField1', 3, '2016-02-22 18:29:01'),
(1, 'asd fasd ádf', 3, '2016-02-22 18:29:04'),
(1, 'jTextField1', 1, '2016-02-23 04:13:33'),
(2, 'jTextField1', 2, '2016-02-23 04:13:37'),
(2, 'jTextField1', 4, '2016-02-23 04:13:58'),
(2, 'cbncvbn', 4, '2016-02-23 04:14:13');

-- --------------------------------------------------------

--
-- Table structure for table `relationship`
--

CREATE TABLE `relationship` (
  `id_user1` int(11) NOT NULL,
  `ID_user2` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Dumping data for table `relationship`
--

INSERT INTO `relationship` (`id_user1`, `ID_user2`, `status`) VALUES
(1, 2, 2),
(1, 3, 2),
(1, 4, 2),
(1, 5, 2),
(1, 6, 2),
(17, 1, 2),
(2, 3, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `conversation`
--
ALTER TABLE `conversation`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ID_user_1` (`ID_user_1`),
  ADD KEY `ID_user_2` (`ID_user_2`);

--
-- Indexes for table `info_user`
--
ALTER TABLE `info_user`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD KEY `ID_user` (`ID_user`),
  ADD KEY `id_conversation` (`id_conversation`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `conversation`
--
ALTER TABLE `conversation`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `info_user`
--
ALTER TABLE `info_user`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `conversation`
--
ALTER TABLE `conversation`
  ADD CONSTRAINT `conversation_ibfk_1` FOREIGN KEY (`ID_user_1`) REFERENCES `info_user` (`ID`),
  ADD CONSTRAINT `conversation_ibfk_2` FOREIGN KEY (`ID_user_2`) REFERENCES `info_user` (`ID`);

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`ID_user`) REFERENCES `info_user` (`ID`),
  ADD CONSTRAINT `message_ibfk_2` FOREIGN KEY (`id_conversation`) REFERENCES `conversation` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
