# Spring Boot RESTful API & AJAX CRUD (Category & Product)

Dự án phát triển ứng dụng web với **Spring Boot** kết nối cơ sở dữ liệu **Microsoft SQL Server**, cung cấp hệ thống **RESTful API** và giao diện render hoàn toàn bằng **jQuery AJAX** cho chức năng CRUD, tìm kiếm và phân trang không tải lại trang.

---

## Tính Năng Nổi Bật

- **RESTful API hoàn chỉnh**:
  - CRUD cho **Category** (Thêm, Sửa, Xóa, Xem chi tiết, Phân trang và Tìm kiếm theo tên).
  - CRUD cho **Product** (Thêm, Sửa, Xóa, Xem chi tiết, Lọc theo danh mục, Phân trang và Tìm kiếm theo tên).
  - Xử lý Upload file (icon danh mục, ảnh sản phẩm) với tên file sinh ngẫu nhiên UUID và lưu vào thư mục `uploads/`.
- **Giao diện AJAX (Single Page Experience)**:
  - Sử dụng **jQuery 3.6.4** kết hợp **Bootstrap 5**.
  - Tải dữ liệu bảng và nạp thanh phân trang hoàn toàn bằng AJAX.
  - Tìm kiếm tức thì theo từ khóa, lọc theo danh mục.
  - Modal Bootstrap cho thao tác Thêm mới và Chỉnh sửa kèm tính năng xem trước ảnh (image preview).
  - Xóa bản ghi với hiệu ứng `fadeOut` mượt mà không reload trang.
- **Tài liệu API tự động**:
  - Tích hợp **Swagger 3 / OpenAPI 3** (`springdoc-openapi-starter-webmvc-ui:2.8.6`).
  - Giao diện Swagger UI tương tác trực tiếp tại `/swagger-ui/index.html`.
- **Cơ sở dữ liệu SQL Server**:
  - File script khởi tạo database, bảng và nạp dữ liệu mẫu nằm tại [`database/init_database.sql`](database/init_database.sql).

---

## Công Nghệ Sử Dụng (Tech Stack)

| Thành phần | Công nghệ | Phiên bản |
| :--- | :--- | :--- |
| **Backend Framework** | Spring Boot | 4.1.1 (Spring 7) |
| **Ngôn ngữ** | Java | 26 |
| **Cơ sở dữ liệu** | Microsoft SQL Server | 2022 / 2025 |
| **ORM / Persistence** | Spring Data JPA / Hibernate | 7.x (Jakarta EE) |
| **Driver JDBC** | MSSQL JDBC Driver | 13.4.0 |
| **Tiện ích File Upload** | Apache Commons IO | 2.16.1 |
| **Tài liệu API** | SpringDoc OpenAPI (Swagger 3) | 2.8.6 |
| **View Engine** | Thymeleaf | 3.x |
| **Frontend Scripting** | jQuery | 3.6.4 |
| **CSS Framework** | Bootstrap | 5.3.3 |

---

## Cấu Trúc Thư Mục

```text
springboot1-7/
├── database/
│   └── init_database.sql          # Script tạo Database springboot1_7_db và dữ liệu mẫu
├── uploads/                       # Thư mục lưu trữ file ảnh / icon upload
├── src/
│   ├── main/
│   │   ├── java/vn/iotstar/
│   │   │   ├── config/            # Cấu hình Storage, ResourceHandler (WebConfig)
│   │   │   ├── controllers/
│   │   │   │   ├── api/           # CategoryAPIController, ProductApiController
│   │   │   │   └── WebController.java  # Điều hướng view AJAX
│   │   │   ├── entity/            # Category.java, Product.java
│   │   │   ├── Exception/         # StorageException, StorageFileNotFoundException
│   │   │   ├── model/             # Response.java, CategoryModel.java, ProductModel.java
│   │   │   ├── repository/        # CategoryRepository, ProductRepository
│   │   │   ├── service/           # ICategoryService, IProductService, IStorageService
│   │   │   │   └── impl/          # Triển khai Service logic
│   │   │   └── Springboot17Application.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/            # Static assets & standalone HTML
│   │       └── templates/         # Thymeleaf AJAX views: index, category-ajax, product-ajax
└── pom.xml
```

---

## Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### 1. Khởi tạo Cơ Sở Dữ Liệu SQL Server
Chạy file script [`database/init_database.sql`](database/init_database.sql) trong SQL Server Management Studio (SSMS) hoặc qua command line:
```bash
sqlcmd -S localhost -U sa -P 123456 -C -i database/init_database.sql
```
*Script sẽ tự động tạo cơ sở dữ liệu `springboot1_7_db`, các bảng `Categories`, `Products` và nạp sẵn dữ liệu mẫu.*

### 2. Cấu hình kết nối trong `application.properties`
Kiểm tra cấu hình tài khoản SQL Server trong file `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=springboot1_7_db;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
spring.datasource.username=sa
spring.datasource.password=123456
```

### 3. Biên dịch và Chạy Ứng Dụng
```bash
# Biên dịch dự án bằng Maven
mvn clean package -DskipTests

# Chạy file JAR đã build
java -jar target/springboot1-7-1.0.jar
```

---

## Danh Sách Đường Dẫn Truy Cập

| Chức năng | Đường dẫn (URL) |
| :--- | :--- |
| **Trang chủ Dashboard** | [http://localhost:8080/](http://localhost:8080/) |
| **Quản lý Category AJAX** | [http://localhost:8080/category](http://localhost:8080/category) |
| **Quản lý Product AJAX** | [http://localhost:8080/product](http://localhost:8080/product) |
| **Swagger 3 / OpenAPI UI** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |

---

## Danh Sách RESTful API

### 1. Category Endpoints (`/api/category`)
- `GET /api/category`: Lấy danh sách tất cả Category.
- `GET /api/category/page?keyword={kw}&page={p}&size={s}`: Tìm kiếm và phân trang Category.
- `GET /api/category/{id}` hoặc `POST /api/category/getCategory?id={id}`: Lấy chi tiết Category.
- `POST /api/category/addCategory`: Thêm Category mới (hỗ trợ multipart file `icon`).
- `PUT /api/category/updateCategory`: Cập nhật Category (hỗ trợ đổi `icon` mới hoặc giữ cũ).
- `DELETE /api/category/deleteCategory?categoryId={id}`: Xóa Category.

### 2. Product Endpoints (`/api/product`)
- `GET /api/product`: Lấy danh sách tất cả Product.
- `GET /api/product/page?keyword={kw}&categoryId={catId}&page={p}&size={s}`: Tìm kiếm theo tên sản phẩm, lọc danh mục và phân trang.
- `GET /api/product/{id}` hoặc `POST /api/product/getProduct?id={id}`: Lấy chi tiết Product.
- `POST /api/product/addProduct`: Thêm Product mới (hỗ trợ multipart file `imageFile`).
- `PUT /api/product/updateProduct`: Cập nhật Product (hỗ trợ đổi ảnh mới).
- `DELETE /api/product/deleteProduct?productId={id}`: Xóa Product.
- `GET /admin/products/images/{filename}`: Xem ảnh sản phẩm trực tiếp.
