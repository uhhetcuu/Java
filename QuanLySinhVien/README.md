PHẦN MỀM QUẢN LÝ SINH VIÊN

Môn học: Đồ án phần mềm Java
Đề tài: Quản lý sinh viên
Ngôn ngữ: Java 17
Giao diện: Java Swing
Cơ sở dữ liệu: SQL Server

1. Giới thiệu

Đồ án này xây dựng một chương trình quản lý sinh viên bằng Java. Chương trình sử dụng SQL Server để lưu dữ liệu. Các chức năng được chia thành bốn màn hình: quản lý sinh viên, quản lý lớp học, quản lý môn học và quản lý bảng điểm. Mỗi màn hình có thể thêm, sửa, xóa và tìm kiếm dữ liệu.

2. Cơ sở dữ liệu

Cơ sở dữ liệu QuanLySinhVienDB gồm bốn bảng. LopHoc lưu mã lớp, tên lớp và niên khóa. SinhVien lưu thông tin sinh viên và mã lớp đang học. MonHoc lưu tên môn học và số tín chỉ. BangDiem lưu điểm chuyên cần, điểm kiểm tra và điểm thi của từng sinh viên theo từng môn học.

Bảng SinhVien liên kết với LopHoc thông qua cột MaLop. Bảng BangDiem liên kết với SinhVien và MonHoc. Hai cột MaSV và MaMon được dùng làm khóa chính ghép của BangDiem.

3. Công nghệ sử dụng

Chương trình sử dụng Java Swing để làm giao diện và JDBC để kết nối SQL Server. Các câu lệnh truy vấn được đặt trong các lớp DAO. Phần giao diện nằm trong thư mục ui, phần kết nối cơ sở dữ liệu nằm trong thư mục config. Các file SQL dùng để tạo bảng và thêm dữ liệu mẫu nằm trong thư mục database.

4. Cài đặt cơ sở dữ liệu

Trước khi chạy chương trình, cần cài JDK 17 trở lên, SQL Server và SQL Server Management Studio. Mở SQL Server Management Studio và kết nối đến máy chủ SQL Server. Sau đó mở file database/01_create_database.sql và chạy để tạo cơ sở dữ liệu. Khi chạy xong, mở tiếp file database/02_sample_data.sql để thêm dữ liệu mẫu.

5. Cấu hình kết nối

Sao chép file config.properties.example và đổi tên bản sao thành config.properties. Mở file này, sau đó sửa tên máy chủ, tài khoản và mật khẩu SQL Server theo máy đang sử dụng. Port mặc định là 1433.

Nội dung file cấu hình:

```text
db.host=localhost
db.port=1433
db.name=QuanLySinhVienDB
db.user=sa
db.password=mat_khau_SQL_Server
db.encrypt=true
db.trustServerCertificate=true
```

Không đưa file config.properties có mật khẩu thật lên GitHub. Repository chỉ cần có file config.properties.example.

6. Chạy chương trình

Mở thư mục QuanLySinhVien bằng IntelliJ IDEA hoặc NetBeans có hỗ trợ Maven. Chờ Maven tải các thư viện cần thiết, sau đó mở lớp com.quanlysinhvien.App và chạy hàm main.

Nếu chạy bằng Terminal, mở Terminal tại thư mục chứa file pom.xml và nhập hai lệnh sau:

```bash
mvn clean package
java -jar target/quan-ly-sinh-vien-1.0.0.jar
```
```bash
git init
git add .
git commit -m "Hoan thanh do an Java quan ly sinh vien"
git branch -M main
git remote add origin https://github.com/USERNAME/REPOSITORY.git
git push -u origin main
```
