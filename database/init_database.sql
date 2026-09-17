-- ====================================================================
-- Script tạo cơ sở dữ liệu và dữ liệu mẫu cho Spring Boot RESTful API & AJAX
-- Database: springboot1_7_db
-- User: sa / 123456
-- ====================================================================

USE master;
GO

-- 1. Tạo Database nếu chưa tồn tại
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'springboot1_7_db')
BEGIN
    CREATE DATABASE springboot1_7_db;
    PRINT N'Database springboot1_7_db đã được tạo thành công.';
END
ELSE
BEGIN
    PRINT N'Database springboot1_7_db đã tồn tại.';
END
GO

USE springboot1_7_db;
GO

-- 2. Tạo bảng Categories
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Categories')
BEGIN
    CREATE TABLE Categories (
        categoryId BIGINT IDENTITY(1,1) PRIMARY KEY,
        categoryName NVARCHAR(255) NOT NULL,
        icon NVARCHAR(500) NULL,
        status SMALLINT DEFAULT 1
    );
    PRINT N'Bảng Categories đã được tạo thành công.';
END
GO

-- 3. Tạo bảng Products
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Products')
BEGIN
    CREATE TABLE Products (
        productId BIGINT IDENTITY(1,1) PRIMARY KEY,
        productName NVARCHAR(500) NOT NULL,
        quantity INT NOT NULL DEFAULT 0,
        unitPrice FLOAT NOT NULL DEFAULT 0,
        images NVARCHAR(500) NULL,
        description NVARCHAR(MAX) NULL,
        discount FLOAT NOT NULL DEFAULT 0,
        createDate DATETIME2 NOT NULL DEFAULT GETDATE(),
        status SMALLINT NOT NULL DEFAULT 1,
        categoryId BIGINT NULL,
        CONSTRAINT FK_Products_Categories FOREIGN KEY (categoryId) 
            REFERENCES Categories(categoryId) ON DELETE SET NULL
    );
    PRINT N'Bảng Products đã được tạo thành công.';
END
GO

-- 4. Thêm dữ liệu mẫu Categories nếu bảng rỗng
IF NOT EXISTS (SELECT 1 FROM Categories)
BEGIN
    SET IDENTITY_INSERT Categories ON;
    INSERT INTO Categories (categoryId, categoryName, icon, status) VALUES
    (1, N'Điện thoại thông minh', N'phone.png', 1),
    (2, N'Laptop & Máy tính xách tay', N'laptop.png', 1),
    (3, N'Máy tính bảng (Tablet)', N'tablet.png', 1),
    (4, N'Tai nghe & Âm thanh', N'headphone.png', 1),
    (5, N'Đồng hồ thông minh & Smartwatch', N'watch.png', 1),
    (6, N'Phụ kiện điện thoại', N'accessories.png', 1),
    (7, N'Thiết bị mạng & Router', N'network.png', 1),
    (8, N'Camera & Thiết bị ghi hình', N'camera.png', 1);
    SET IDENTITY_INSERT Categories OFF;
    PRINT N'Đã nạp 8 danh mục mẫu vào bảng Categories.';
END
GO

-- 5. Thêm dữ liệu mẫu Products nếu bảng rỗng
IF NOT EXISTS (SELECT 1 FROM Products)
BEGIN
    SET IDENTITY_INSERT Products ON;
    INSERT INTO Products (productId, productName, quantity, unitPrice, images, description, discount, createDate, status, categoryId) VALUES
    (1, N'iPhone 16 Pro Max 256GB', 45, 34990000, N'iphone-16-pro-max.jpg', N'Chip Apple A18 Pro mạnh mẽ, khung titan sang trọng, camera zoom quang học 5x.', 5, GETDATE(), 1, 1),
    (2, N'Samsung Galaxy S24 Ultra 512GB', 30, 31990000, N'galaxy-s24-ultra.jpg', N'Tích hợp Galaxy AI thông minh, bút S-Pen quyền năng, camera 200MP siêu sắc nét.', 8, GETDATE(), 1, 1),
    (3, N'Xiaomi 14 Ultra 5G', 25, 26990000, N'xiaomi-14-ultra.jpg', N'Ống kính quang học Leica đỉnh cao, chip Snapdragon 8 Gen 3, màn hình AMOLED 2K.', 10, GETDATE(), 1, 1),
    (4, N'MacBook Pro 14 M3 Pro (18GB/512GB)', 20, 49990000, N'macbook-pro-14-m3.jpg', N'Hiệu năng đột phá với chip M3 Pro, màn hình Liquid Retina XDR 120Hz mượt mà.', 6, GETDATE(), 1, 2),
    (5, N'Dell XPS 13 Plus 9320 Core i7', 15, 38500000, N'dell-xps-13-plus.jpg', N'Thiết kế tương lai siêu mỏng nhẹ, màn hình cảm ứng OLED 3.5K sắc nét.', 5, GETDATE(), 1, 2),
    (6, N'Asus ROG Zephyrus G16 RTX 4070', 12, 54990000, N'asus-rog-g16.jpg', N'Laptop gaming cao cấp màn hình OLED 240Hz, Core Ultra 9, tản nhiệt buồng hơi.', 7, GETDATE(), 1, 2),
    (7, N'iPad Pro 11 M4 OLED 256GB', 35, 28990000, N'ipad-pro-m4.jpg', N'Độ mỏng kỷ lục 5.3mm, màn hình Ultra Retina XDR Tandem OLED, chip Apple M4.', 4, GETDATE(), 1, 3),
    (8, N'Samsung Galaxy Tab S9 Ultra', 18, 24500000, N'tab-s9-ultra.jpg', N'Màn hình cực đại 14.6 inch Dynamic AMOLED 2X, kháng nước bụi chuẩn IP68.', 12, GETDATE(), 1, 3),
    (9, N'Sony WH-1000XM5 Chống ồn', 50, 7990000, N'sony-wh1000xm5.jpg', N'Tai nghe trùm đầu chống ồn chủ động hàng đầu thế giới, thời lượng pin 30 giờ.', 15, GETDATE(), 1, 4),
    (10, N'AirPods Pro 2 MagSafe USB-C', 60, 5690000, N'airpods-pro-2.jpg', N'Khử tiếng ồn chủ động gấp 2 lần, âm thanh thích ứng và cổng sạc USB-C tiện lợi.', 10, GETDATE(), 1, 4),
    (11, N'Apple Watch Ultra 2 GPS + Cellular 49mm', 22, 21490000, N'apple-watch-ultra-2.jpg', N'Vỏ titan siêu bền bỉ, màn hình 3000 nits siêu sáng, GPS băng tần kép chính xác.', 5, GETDATE(), 1, 5),
    (12, N'Samsung Galaxy Watch 7 44mm Bluetooth', 28, 7490000, N'galaxy-watch-7.jpg', N'Cảm biến BioActive thế hệ mới theo dõi sức khỏe chuyên sâu, chip 3nm tiết kiệm pin.', 8, GETDATE(), 1, 5),
    (13, N'Củ sạc nhanh Anker 65W GaNPrime 3 cổng', 100, 890000, N'anker-gan-65w.jpg', N'Công nghệ sạc GaN nhỏ gọn, sạc cùng lúc 3 thiết bị laptop, tablet và điện thoại.', 20, GETDATE(), 1, 6),
    (14, N'Pin sạc dự phòng MagSafe 10000mAh', 80, 750000, N'pin-du-phong-magsafe.jpg', N'Hít nam châm từ tính chắc chắn cho iPhone, sạc nhanh không dây 15W.', 15, GETDATE(), 1, 6),
    (15, N'Router Wi-Fi 7 ASUS RT-BE88U', 10, 8900000, N'asus-wifi-7-router.jpg', N'Băng thông siêu tốc lên đến 7200Mbps, 10 cổng Ethernet đa tốc độ.', 10, GETDATE(), 1, 7);
    SET IDENTITY_INSERT Products OFF;
    PRINT N'Đã nạp 15 sản phẩm mẫu vào bảng Products.';
END
GO

PRINT N'Khởi tạo cơ sở dữ liệu springboot1_7_db thành công!';
GO
