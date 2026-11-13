/* Du lieu mau de kiem tra cac chuc nang. Co the chay lai an toan. */
USE QuanLySinhVienDB;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.LopHoc WHERE MaLop = N'CNTT1')
    INSERT INTO dbo.LopHoc(MaLop, TenLop, NienKhoa) VALUES (N'CNTT1', N'Công nghệ thông tin 1', N'2023-2027');
IF NOT EXISTS (SELECT 1 FROM dbo.LopHoc WHERE MaLop = N'CNTT2')
    INSERT INTO dbo.LopHoc(MaLop, TenLop, NienKhoa) VALUES (N'CNTT2', N'Công nghệ thông tin 2', N'2023-2027');
IF NOT EXISTS (SELECT 1 FROM dbo.LopHoc WHERE MaLop = N'QTKD1')
    INSERT INTO dbo.LopHoc(MaLop, TenLop, NienKhoa) VALUES (N'QTKD1', N'Quản trị kinh doanh 1', N'2024-2028');
GO

IF NOT EXISTS (SELECT 1 FROM dbo.MonHoc WHERE MaMon = N'JAVA01')
    INSERT INTO dbo.MonHoc(MaMon, TenMon, SoTinChi) VALUES (N'JAVA01', N'Lập trình Java', 3);
IF NOT EXISTS (SELECT 1 FROM dbo.MonHoc WHERE MaMon = N'CSDL01')
    INSERT INTO dbo.MonHoc(MaMon, TenMon, SoTinChi) VALUES (N'CSDL01', N'Cơ sở dữ liệu', 3);
IF NOT EXISTS (SELECT 1 FROM dbo.MonHoc WHERE MaMon = N'THVP01')
    INSERT INTO dbo.MonHoc(MaMon, TenMon, SoTinChi) VALUES (N'THVP01', N'Tin học văn phòng', 2);
GO

IF NOT EXISTS (SELECT 1 FROM dbo.SinhVien WHERE MaSV = N'SV001')
    INSERT INTO dbo.SinhVien(MaSV, HoTen, NgaySinh, GioiTinh, Email, MaLop)
    VALUES (N'SV001', N'Nguyễn Văn An', '2005-03-12', N'Nam', N'an@example.com', N'CNTT1');
IF NOT EXISTS (SELECT 1 FROM dbo.SinhVien WHERE MaSV = N'SV002')
    INSERT INTO dbo.SinhVien(MaSV, HoTen, NgaySinh, GioiTinh, Email, MaLop)
    VALUES (N'SV002', N'Trần Thị Bình', '2005-07-21', N'Nữ', N'binh@example.com', N'CNTT1');
IF NOT EXISTS (SELECT 1 FROM dbo.SinhVien WHERE MaSV = N'SV003')
    INSERT INTO dbo.SinhVien(MaSV, HoTen, NgaySinh, GioiTinh, Email, MaLop)
    VALUES (N'SV003', N'Lê Minh Châu', '2006-01-08', N'Nam', N'chau@example.com', N'CNTT2');
GO

IF NOT EXISTS (SELECT 1 FROM dbo.BangDiem WHERE MaSV = N'SV001' AND MaMon = N'JAVA01')
    INSERT INTO dbo.BangDiem(MaSV, MaMon, DiemChuyenCan, DiemKiemTra, DiemThi)
    VALUES (N'SV001', N'JAVA01', 9, 8, 8.5);
IF NOT EXISTS (SELECT 1 FROM dbo.BangDiem WHERE MaSV = N'SV001' AND MaMon = N'CSDL01')
    INSERT INTO dbo.BangDiem(MaSV, MaMon, DiemChuyenCan, DiemKiemTra, DiemThi)
    VALUES (N'SV001', N'CSDL01', 8, 7.5, 8);
IF NOT EXISTS (SELECT 1 FROM dbo.BangDiem WHERE MaSV = N'SV002' AND MaMon = N'JAVA01')
    INSERT INTO dbo.BangDiem(MaSV, MaMon, DiemChuyenCan, DiemKiemTra, DiemThi)
    VALUES (N'SV002', N'JAVA01', 9.5, 9, 9);
GO

PRINT N'Da nap du lieu mau.';
GO
