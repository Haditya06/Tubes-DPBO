-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 24, 2026 at 02:25 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hazzelnut`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id_customer` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id_customer`, `id_user`, `alamat`, `email`, `no_hp`) VALUES
(1, 1, 'sby', NULL, '09212312121'),
(2, 2, 'surabaya', NULL, '09123412231'),
(3, 9, 'surabaya', NULL, '09878723443'),
(4, 10, 'tangerang', NULL, '0987'),
(5, 13, 'bandung', NULL, '098989212');

-- --------------------------------------------------------

--
-- Table structure for table `detail_transaksi`
--

CREATE TABLE `detail_transaksi` (
  `id_detail` int(11) NOT NULL,
  `id_pemeriksaan` int(11) DEFAULT NULL,
  `id_obat` int(11) DEFAULT NULL,
  `qty` int(11) NOT NULL,
  `harga_satuan` double NOT NULL,
  `subtotal` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_transaksi`
--

INSERT INTO `detail_transaksi` (`id_detail`, `id_pemeriksaan`, `id_obat`, `qty`, `harga_satuan`, `subtotal`) VALUES
(1, 12, 1, 2, 15000, 30000),
(2, 12, 2, 3, 5000, 15000),
(3, 13, 1, 2, 10000, 20000),
(4, 11, 1, 2, 10000, 20000),
(5, 16, 1, 1, 15000, 15000),
(6, 16, 5, 1, 8000, 8000),
(7, 17, 5, 1, 8000, 8000),
(8, 17, 9, 1, 35000, 35000),
(9, 17, 7, 1, 20000, 20000),
(10, 18, 5, 1, 8000, 8000),
(11, 15, 1, 2, 10000, 20000),
(12, 15, 2, 1, 15000, 15000),
(13, 19, 5, 2, 8000, 16000),
(14, 20, 1, 1, 15000, 15000),
(15, 20, 1, 1, 15000, 15000),
(16, 20, 7, 1, 20000, 20000),
(17, 21, 8, 1, 22000, 22000),
(18, 21, 9, 1, 35000, 35000),
(19, 22, 5, 1, 8000, 8000);

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

CREATE TABLE `dokter` (
  `id_dokter` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `spesialisasi` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`id_dokter`, `id_user`, `spesialisasi`) VALUES
(1, 5, 'Dokter Hewan Umum'),
(2, 6, 'Bedah Hewan'),
(3, 7, 'Penyakit Dalam Hewan'),
(4, 8, 'Hewan Eksotik & Unggas');

-- --------------------------------------------------------

--
-- Table structure for table `hewan`
--

CREATE TABLE `hewan` (
  `id_hewan` int(11) NOT NULL,
  `id_customer` int(11) DEFAULT NULL,
  `nama_hewan` varchar(100) DEFAULT NULL,
  `jenis` varchar(50) DEFAULT NULL,
  `umur` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hewan`
--

INSERT INTO `hewan` (`id_hewan`, `id_customer`, `nama_hewan`, `jenis`, `umur`) VALUES
(1, 1, 'uni', 'cat', 12),
(2, 2, 'meme', 'dog', 4),
(3, 1, 'kambilngnjr', 'kambing', 2),
(4, 1, 'kambing', '1223', 23),
(5, 1, 'domba', 'domba', 12),
(6, 1, 'cek', 'kucing lucu', 1),
(7, 1, 'luei', 'ular', 1),
(8, 1, 'kucing', 'kucing', 1),
(9, 1, 'tese', 'met', 1),
(10, 1, 'anjing guguk', 'anging', 12),
(11, 3, 'koko', 'kucing', 2),
(18, 1, 'nino', 'kucing', 12),
(19, 1, 'noo', 'anjing', 1),
(20, 1, 'guguk', 'ikan', 2),
(23, 4, 'tuan', 'ikan', 1),
(24, 4, 'nino', 'anjing', 12),
(25, 5, 'kini', 'ikan', 2),
(26, 5, 'kiko', 'ikan', 1);

-- --------------------------------------------------------

--
-- Table structure for table `layanan`
--

CREATE TABLE `layanan` (
  `id_layanan` int(11) NOT NULL,
  `nama_layanan` varchar(100) NOT NULL,
  `harga` double NOT NULL,
  `deskripsi` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `layanan`
--

INSERT INTO `layanan` (`id_layanan`, `nama_layanan`, `harga`, `deskripsi`) VALUES
(1, 'Pemeriksaan Umum', 100000, 'Pemeriksaan kesehatan rutin'),
(2, 'Vaksinasi', 150000, 'Pemberian vaksin untuk hewan'),
(3, 'Sterilisasi', 500000, 'Operasi sterilisasi kucing/anjing'),
(4, 'Perawatan Gigi', 200000, 'Pembersihan karang gigi'),
(5, 'Suntik Antibiotik', 75000, 'Pemberian antibiotik'),
(6, 'Obat Cacing', 50000, 'Pemberian obat cacing');

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `id_obat` int(11) NOT NULL,
  `nama_obat` varchar(100) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `harga` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`id_obat`, `nama_obat`, `stok`, `harga`) VALUES
(1, 'Amoxicillin 500mg', 97, 15000),
(2, 'Paracetamol 500mg', 150, 5000),
(3, 'Ibuprofen 400mg', 80, 12000),
(4, 'Vitamin C 1000mg', 200, 25000),
(5, 'Antasida', 114, 8000),
(6, 'Cetirizine 10mg', 90, 18000),
(7, 'Dexamethasone 0.5mg', 58, 20000),
(8, 'Metronidazole 500mg', 74, 22000),
(9, 'Ciprofloxacin 500mg', 48, 35000),
(10, 'Ranitidine 150mg', 100, 15000);

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id_pembayaran` int(11) NOT NULL,
  `id_tagihan` int(11) DEFAULT NULL,
  `total_bayar` double DEFAULT NULL,
  `tanggal_bayar` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pembayaran`
--

INSERT INTO `pembayaran` (`id_pembayaran`, `id_tagihan`, `total_bayar`, `tanggal_bayar`) VALUES
(1, 1, 35000, '2026-06-24 17:42:15'),
(2, 2, 50000, '2026-06-24 17:50:29'),
(3, 3, 50000, '2026-06-24 17:50:41'),
(4, 4, 63000, '2026-06-24 17:52:14'),
(5, 5, 36000, '2026-06-24 17:58:16'),
(6, 6, 57000, '2026-06-24 18:03:25'),
(7, 7, 8000, '2026-06-24 18:12:15');

-- --------------------------------------------------------

--
-- Table structure for table `pemeriksaan`
--

CREATE TABLE `pemeriksaan` (
  `id_pemeriksaan` int(11) NOT NULL,
  `id_reservasi` int(11) DEFAULT NULL,
  `id_dokter` int(11) DEFAULT NULL,
  `id_layanan` int(11) DEFAULT NULL,
  `diagnosis` text DEFAULT NULL,
  `tindakan` text DEFAULT NULL,
  `biaya_layanan` double NOT NULL DEFAULT 0,
  `tanggal_periksa` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pemeriksaan`
--

INSERT INTO `pemeriksaan` (`id_pemeriksaan`, `id_reservasi`, `id_dokter`, `id_layanan`, `diagnosis`, `tindakan`, `biaya_layanan`, `tanggal_periksa`) VALUES
(11, 9, 1, NULL, 'tes12', 'tes12', 20000, '2026-06-16 22:32:00'),
(12, 1, 1, NULL, 'kanker ni', 'mutilasi', 100000, '2026-06-17 06:53:04'),
(13, 4, 1, NULL, 'sakit campak', 'suntik rabies', 200000, '2026-06-17 09:01:29'),
(15, 3, 1, NULL, 'sakit gigi', 'melakukan suntik gigi', 100000, '2026-06-18 11:24:59'),
(16, 8, 1, NULL, 'sakit gigi', 'cabut gigi', 50000, '2026-06-18 13:20:58'),
(17, 10, 1, NULL, 'patah kaki', 'oprasii berat', 200000, '2026-06-18 15:13:19'),
(18, 13, 1, NULL, 'sakit campak', 'sakit rabius', 210000, '2026-06-24 17:03:39'),
(19, 4, 1, NULL, 'sakit campak', 'operasi', 200000, '2026-06-24 17:43:54'),
(20, 14, 1, NULL, 'sakit', 'operasi', 200000, '2026-06-24 17:46:42'),
(21, 15, 1, NULL, 'sakit kaki', 'di amplas', 130000, '2026-06-24 18:03:05'),
(22, 16, 1, NULL, 'sakit gigi ringan', 'cabut gigi geraham', 100000, '2026-06-24 18:11:51');

-- --------------------------------------------------------

--
-- Table structure for table `rekam_medis`
--

CREATE TABLE `rekam_medis` (
  `id_rekam` int(11) NOT NULL,
  `id_hewan` int(11) DEFAULT NULL,
  `id_pemeriksaan` int(11) DEFAULT NULL,
  `riwayat_penyakit` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rekam_medis`
--

INSERT INTO `rekam_medis` (`id_rekam`, `id_hewan`, `id_pemeriksaan`, `riwayat_penyakit`) VALUES
(1, 3, 19, 'sakit campak'),
(2, 23, 20, 'sakit'),
(3, 24, 21, 'sakit kaki'),
(4, 25, 22, 'sakit gigi ringan');

-- --------------------------------------------------------

--
-- Table structure for table `reservasi`
--

CREATE TABLE `reservasi` (
  `id_reservasi` int(11) NOT NULL,
  `id_customer` int(11) DEFAULT NULL,
  `id_hewan` int(11) DEFAULT NULL,
  `tanggal` datetime DEFAULT NULL,
  `status_reservasi` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservasi`
--

INSERT INTO `reservasi` (`id_reservasi`, `id_customer`, `id_hewan`, `tanggal`, `status_reservasi`) VALUES
(1, 1, 1, '2026-08-07 14:00:11', 'LUNAS'),
(2, 1, 3, '2026-09-08 12:00:00', 'SELESAI'),
(3, 1, 4, '2027-09-02 13:00:00', 'LUNAS'),
(4, 1, 3, '2027-09-30 12:12:12', 'LUNAS'),
(5, 1, 3, '2022-09-03 12:00:00', 'MENUNGGU'),
(6, 1, 1, '2020-04-23 12:12:12', 'MENUNGGU'),
(7, 1, 1, '2026-07-07 12:12:12', 'MENUNGGU'),
(8, 1, 1, '2020-09-02 12:13:14', 'LUNAS'),
(9, 1, 7, '2027-09-03 12:02:30', 'LUNAS'),
(10, 1, 7, '2027-07-09 12:20:00', 'LUNAS'),
(11, 1, 1, '2026-07-04 12:30:12', 'MENUNGGU'),
(13, 4, 23, '2026-07-01 12:00:00', 'LUNAS'),
(14, 4, 23, '2026-07-01 20:21:10', 'LUNAS'),
(15, 4, 24, '2027-07-01 12:00:00', 'LUNAS'),
(16, 5, 25, '2026-07-01 12:12:12', 'LUNAS'),
(18, 5, 25, '2026-09-01 12:00:00', 'PENDING'),
(19, 5, 26, '2027-08-02 12:00:00', 'PENDING');

-- --------------------------------------------------------

--
-- Table structure for table `reservasi_layanan`
--

CREATE TABLE `reservasi_layanan` (
  `id_reservasi_layanan` int(11) NOT NULL,
  `id_reservasi` int(11) NOT NULL,
  `id_layanan` int(11) NOT NULL,
  `harga_satuan` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservasi_layanan`
--

INSERT INTO `reservasi_layanan` (`id_reservasi_layanan`, `id_reservasi`, `id_layanan`, `harga_satuan`) VALUES
(1, 1, 1, 100000.00),
(2, 1, 2, 150000.00),
(3, 18, 2, 150000.00),
(4, 18, 4, 200000.00),
(5, 19, 1, 100000.00),
(6, 19, 3, 500000.00);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `id_staff` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `jabatan` varchar(50) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`id_staff`, `id_user`, `jabatan`, `no_hp`, `alamat`) VALUES
(1, 3, 'Administrator', '081234567890', 'Jakarta Selatan');

-- --------------------------------------------------------

--
-- Table structure for table `tagihan`
--

CREATE TABLE `tagihan` (
  `id_tagihan` int(11) NOT NULL,
  `id_pemeriksaan` int(11) DEFAULT NULL,
  `total` double DEFAULT NULL,
  `status_bayar` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tagihan`
--

INSERT INTO `tagihan` (`id_tagihan`, `id_pemeriksaan`, `total`, `status_bayar`) VALUES
(1, 15, 35000, 'LUNAS'),
(2, 20, 50000, 'LUNAS'),
(3, 20, 50000, 'LUNAS'),
(4, 17, 63000, 'LUNAS'),
(5, 13, 36000, 'LUNAS'),
(6, 21, 57000, 'LUNAS'),
(7, 22, 8000, 'LUNAS');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `role` enum('CUSTOMER','DOKTER','STAFF') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `username`, `password`, `nama_lengkap`, `role`) VALUES
(1, 'syahril123', 'pass123', 'syahril', 'CUSTOMER'),
(2, 'aril', '123456', 'aril123', 'CUSTOMER'),
(3, 'admin1', 'pass123', 'Budi Santoso', 'STAFF'),
(5, 'dr_andi', 'dokter123', 'Dr. Andi Wijaya', 'DOKTER'),
(6, 'dr_budi', 'dokter123', 'Dr. Budi Santoso', 'DOKTER'),
(7, 'dr_cindy', 'dokter123', 'Dr. Cindy Lestari', 'DOKTER'),
(8, 'dr_dedi', 'dokter123', 'Dr. Dedi Kurniawan', 'DOKTER'),
(9, 'adit', 'pass123', 'Haditiya', 'CUSTOMER'),
(10, 'rayy', '123', 'ray', 'CUSTOMER'),
(13, 'adit123', '123', 'aditdi', 'CUSTOMER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id_customer`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_pemeriksaan` (`id_pemeriksaan`),
  ADD KEY `id_obat` (`id_obat`);

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id_dokter`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `hewan`
--
ALTER TABLE `hewan`
  ADD PRIMARY KEY (`id_hewan`),
  ADD KEY `id_customer` (`id_customer`);

--
-- Indexes for table `layanan`
--
ALTER TABLE `layanan`
  ADD PRIMARY KEY (`id_layanan`);

--
-- Indexes for table `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`id_obat`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `id_tagihan` (`id_tagihan`);

--
-- Indexes for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  ADD PRIMARY KEY (`id_pemeriksaan`),
  ADD KEY `id_reservasi` (`id_reservasi`),
  ADD KEY `id_dokter` (`id_dokter`),
  ADD KEY `id_layanan` (`id_layanan`);

--
-- Indexes for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD PRIMARY KEY (`id_rekam`),
  ADD KEY `id_hewan` (`id_hewan`),
  ADD KEY `id_pemeriksaan` (`id_pemeriksaan`);

--
-- Indexes for table `reservasi`
--
ALTER TABLE `reservasi`
  ADD PRIMARY KEY (`id_reservasi`),
  ADD KEY `id_customer` (`id_customer`),
  ADD KEY `id_hewan` (`id_hewan`);

--
-- Indexes for table `reservasi_layanan`
--
ALTER TABLE `reservasi_layanan`
  ADD PRIMARY KEY (`id_reservasi_layanan`),
  ADD KEY `id_reservasi` (`id_reservasi`),
  ADD KEY `id_layanan` (`id_layanan`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`id_staff`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `tagihan`
--
ALTER TABLE `tagihan`
  ADD PRIMARY KEY (`id_tagihan`),
  ADD KEY `id_pemeriksaan` (`id_pemeriksaan`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id_customer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `dokter`
--
ALTER TABLE `dokter`
  MODIFY `id_dokter` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `hewan`
--
ALTER TABLE `hewan`
  MODIFY `id_hewan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `layanan`
--
ALTER TABLE `layanan`
  MODIFY `id_layanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `obat`
--
ALTER TABLE `obat`
  MODIFY `id_obat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `pembayaran`
--
ALTER TABLE `pembayaran`
  MODIFY `id_pembayaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  MODIFY `id_pemeriksaan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  MODIFY `id_rekam` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `reservasi`
--
ALTER TABLE `reservasi`
  MODIFY `id_reservasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `reservasi_layanan`
--
ALTER TABLE `reservasi_layanan`
  MODIFY `id_reservasi_layanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `staff`
--
ALTER TABLE `staff`
  MODIFY `id_staff` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tagihan`
--
ALTER TABLE `tagihan`
  MODIFY `id_tagihan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `detail_transaksi`
--
ALTER TABLE `detail_transaksi`
  ADD CONSTRAINT `detail_transaksi_ibfk_1` FOREIGN KEY (`id_pemeriksaan`) REFERENCES `pemeriksaan` (`id_pemeriksaan`),
  ADD CONSTRAINT `detail_transaksi_ibfk_2` FOREIGN KEY (`id_obat`) REFERENCES `obat` (`id_obat`);

--
-- Constraints for table `dokter`
--
ALTER TABLE `dokter`
  ADD CONSTRAINT `dokter_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `hewan`
--
ALTER TABLE `hewan`
  ADD CONSTRAINT `hewan_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`);

--
-- Constraints for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD CONSTRAINT `pembayaran_ibfk_1` FOREIGN KEY (`id_tagihan`) REFERENCES `tagihan` (`id_tagihan`);

--
-- Constraints for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  ADD CONSTRAINT `pemeriksaan_ibfk_1` FOREIGN KEY (`id_reservasi`) REFERENCES `reservasi` (`id_reservasi`),
  ADD CONSTRAINT `pemeriksaan_ibfk_2` FOREIGN KEY (`id_dokter`) REFERENCES `dokter` (`id_dokter`),
  ADD CONSTRAINT `pemeriksaan_ibfk_3` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`);

--
-- Constraints for table `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD CONSTRAINT `rekam_medis_ibfk_1` FOREIGN KEY (`id_hewan`) REFERENCES `hewan` (`id_hewan`),
  ADD CONSTRAINT `rekam_medis_ibfk_2` FOREIGN KEY (`id_pemeriksaan`) REFERENCES `pemeriksaan` (`id_pemeriksaan`);

--
-- Constraints for table `reservasi`
--
ALTER TABLE `reservasi`
  ADD CONSTRAINT `reservasi_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id_customer`),
  ADD CONSTRAINT `reservasi_ibfk_2` FOREIGN KEY (`id_hewan`) REFERENCES `hewan` (`id_hewan`);

--
-- Constraints for table `reservasi_layanan`
--
ALTER TABLE `reservasi_layanan`
  ADD CONSTRAINT `reservasi_layanan_ibfk_1` FOREIGN KEY (`id_reservasi`) REFERENCES `reservasi` (`id_reservasi`) ON DELETE CASCADE,
  ADD CONSTRAINT `reservasi_layanan_ibfk_2` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`);

--
-- Constraints for table `staff`
--
ALTER TABLE `staff`
  ADD CONSTRAINT `staff_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE;

--
-- Constraints for table `tagihan`
--
ALTER TABLE `tagihan`
  ADD CONSTRAINT `tagihan_ibfk_1` FOREIGN KEY (`id_pemeriksaan`) REFERENCES `pemeriksaan` (`id_pemeriksaan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
