/*
 * Do an Java - Quan ly sinh vien
 * Chay file nay truoc khi chay ung dung Java.
 * Tao co so du lieu va 4 bang theo tai lieu thiet ke.
 */
IF DB_ID(N'QuanLySinhVienDB') IS NULL
BEGIN
    CREATE DATABASE QuanLySinhVienDB;
END;
GO

USE QuanLySinhVienDB;
GO

IF OBJECT_ID(N'dbo.LopHoc', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.LopHoc (
        MaLop NVARCHAR(20) NOT NULL PRIMARY KEY,
        TenLop NVARCHAR(100) NOT NULL,
        NienKhoa NVARCHAR(20) NULL
    );
END;
GO

IF OBJECT_ID(N'dbo.SinhVien', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.SinhVien (
        MaSV NVARCHAR(20) NOT NULL PRIMARY KEY,
        HoTen NVARCHAR(100) NOT NULL,
        NgaySinh DATE NULL,
        GioiTinh NVARCHAR(10) NULL,
        Email NVARCHAR(100) NULL,
        MaLop NVARCHAR(20) NULL,
        CONSTRAINT FK_SinhVien_LopHoc FOREIGN KEY (MaLop)
            REFERENCES dbo.LopHoc(MaLop)
    );
END;
GO

IF OBJECT_ID(N'dbo.MonHoc', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.MonHoc (
        MaMon NVARCHAR(20) NOT NULL PRIMARY KEY,
        TenMon NVARCHAR(100) NOT NULL,
        SoTinChi INT NULL CHECK (SoTinChi > 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.BangDiem', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.BangDiem (
        MaSV NVARCHAR(20) NOT NULL,
        MaMon NVARCHAR(20) NOT NULL,
        DiemChuyenCan FLOAT NULL CHECK (DiemChuyenCan BETWEEN 0 AND 10),
        DiemKiemTra FLOAT NULL CHECK (DiemKiemTra BETWEEN 0 AND 10),
        DiemThi FLOAT NULL CHECK (DiemThi BETWEEN 0 AND 10),
        CONSTRAINT PK_BangDiem PRIMARY KEY (MaSV, MaMon),
        CONSTRAINT FK_BangDiem_SinhVien FOREIGN KEY (MaSV)
            REFERENCES dbo.SinhVien(MaSV),
        CONSTRAINT FK_BangDiem_MonHoc FOREIGN KEY (MaMon)
            REFERENCES dbo.MonHoc(MaMon)
    );
END;
GO

PRINT N'Da tao hoac kiem tra xong database QuanLySinhVienDB va 4 bang.';
GO
