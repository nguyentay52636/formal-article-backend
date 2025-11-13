# 📚 Hướng Dẫn Tạo User Mới - Từ Cơ Bản

## ❓ Câu Hỏi 1: DB đã có dữ liệu, dùng Hibernate có sao không?

### ✅ **KHÔNG SAO!** Nhưng cần cấu hình đúng:

**Đã cập nhật trong `application.properties`:**
```properties
spring.jpa.hibernate.ddl-auto=validate
```

**Giải thích các chế độ:**
- ✅ **`validate`**: Chỉ kiểm tra schema có khớp với entity không, **KHÔNG thay đổi gì** → An toàn cho DB đã có dữ liệu
- ⚠️ **`update`**: Tự động thêm/sửa/xóa cột → **NGUY HIỂM** với DB đã có dữ liệu
- ✅ **`none`**: Không kiểm tra gì → Nhanh nhất, nhưng không báo lỗi nếu schema sai
- ❌ **`create`**: Xóa và tạo lại toàn bộ → **MẤT HẾT DỮ LIỆU!**

**Kết luận:** Với DB đã có dữ liệu, dùng `validate` là an toàn nhất! ✅

---

## ❓ Câu Hỏi 2: Làm sao để tạo User mới?

### 🎯 **Có 3 cách:**

### **Cách 1: Dùng Service (KHUYẾN NGHỊ) - Đơn giản nhất**

```java
@Autowired
private UserService userService;

// Tạo user mới
User newUser = userService.createUser(
    "test@example.com",  // email
    "123456",            // password (sẽ tự động mã hóa)
    "Nguyễn Văn A"       // fullName
);
```

**Ưu điểm:**
- ✅ Tự động kiểm tra email trùng
- ✅ Tự động mã hóa password
- ✅ Tự động set `active = true`
- ✅ Tự động set `createdAt`, `updatedAt`

---

### **Cách 2: Dùng Repository trực tiếp**

```java
@Autowired
private UserRepository userRepository;

@Autowired
private PasswordEncoder passwordEncoder;

// Tạo đối tượng User
User user = new User();
user.setEmail("test@example.com");
user.setPassword(passwordEncoder.encode("123456")); // Phải tự mã hóa
user.setFullName("Nguyễn Văn A");
user.setActive(true);

// Lưu vào database
User savedUser = userRepository.save(user);
```

**Nhược điểm:**
- ❌ Phải tự mã hóa password
- ❌ Phải tự kiểm tra email trùng
- ❌ Phải tự set các giá trị mặc định

---

### **Cách 3: Dùng REST API (Qua Controller)**

**Gửi POST request:**
```bash
POST http://localhost:8000/api/users
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "123456",
  "fullName": "Nguyễn Văn A"
}
```

**Hoặc dùng cURL:**
```bash
curl -X POST http://localhost:8000/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "123456",
    "fullName": "Nguyễn Văn A"
  }'
```

---

## 🔗 Quan Hệ (Relationships) - Có cần dùng `.add()` không?

### ❌ **KHÔNG CẦN!** Hibernate tự động quản lý quan hệ.

### **Ví dụ: Tạo User và gán Avatar**

```java
@Autowired
private UserService userService;

@Autowired
private FileUploadRepository fileUploadRepository;

// Cách 1: Tạo user trước, sau đó gán avatar
User user = userService.createUser("test@example.com", "123456", "Nguyễn Văn A");

// Lấy avatar từ database
FileUpload avatar = fileUploadRepository.findById(1L).orElse(null);

// Gán avatar cho user
user.setAvatar(avatar);
userService.updateUser(user); // Lưu lại

// Cách 2: Tạo user với avatar ngay từ đầu
User user = new User();
user.setEmail("test@example.com");
user.setPassword("123456");
user.setFullName("Nguyễn Văn A");

FileUpload avatar = fileUploadRepository.findById(1L).orElse(null);
user.setAvatar(avatar);

userService.createUser(user); // Service sẽ tự mã hóa password
```

### **Ví dụ: Tạo User và GeneratedCv**

```java
@Autowired
private UserService userService;

@Autowired
private TemplateRepository templateRepository;

// Tạo user
User user = userService.createUser("test@example.com", "123456", "Nguyễn Văn A");

// Lấy template
Template template = templateRepository.findById(1L).orElseThrow();

// Tạo GeneratedCv
GeneratedCv cv = new GeneratedCv();
cv.setUser(user);           // Gán user
cv.setTemplate(template);    // Gán template
cv.setDataJson("{...}");     // Set data JSON
cv.setStyleJson("{...}");    // Set style JSON

// Lưu vào database
generatedCvRepository.save(cv);

// KHÔNG CẦN: user.getGeneratedCvs().add(cv)
// Hibernate tự động quản lý quan hệ!
```

**Lưu ý:**
- ✅ Chỉ cần set quan hệ ở 1 phía (thường là phía "nhiều" - ManyToOne)
- ✅ Hibernate tự động cập nhật phía còn lại
- ❌ Không cần dùng `.add()` trừ khi bạn muốn thao tác với collection

---

## 📝 Ví Dụ Đầy Đủ: Tạo User Mới Trong Service/Controller

### **Trong Service:**

```java
@Service
public class MyService {
    
    @Autowired
    private UserService userService;
    
    public void createNewUser() {
        // Tạo user mới
        User user = userService.createUser(
            "newuser@example.com",
            "password123",
            "Nguyễn Văn B"
        );
        
        System.out.println("User đã được tạo với ID: " + user.getId());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Created at: " + user.getCreatedAt());
    }
}
```

### **Trong Controller:**

```java
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> request) {
        User user = userService.createUser(
            request.get("email"),
            request.get("password"),
            request.get("fullName")
        );
        return ResponseEntity.ok(user);
    }
}
```

---

## 🧪 Test Thử Nghiệm

### **1. Test bằng REST API:**

```bash
# Tạo user mới
curl -X POST http://localhost:8000/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "123456",
    "fullName": "Nguyễn Văn Test"
  }'

# Lấy tất cả users
curl http://localhost:8000/api/users

# Lấy user theo ID
curl http://localhost:8000/api/users/1
```

### **2. Test bằng Swagger UI:**

1. Mở trình duyệt: `http://localhost:8000/swagger-ui.html`
2. Tìm endpoint `POST /api/users`
3. Click "Try it out"
4. Nhập thông tin user
5. Click "Execute"

---

## ⚠️ Lưu Ý Quan Trọng

1. **Password sẽ được mã hóa tự động** khi dùng `UserService.createUser()`
2. **Email phải unique** - nếu trùng sẽ báo lỗi
3. **CreatedAt và UpdatedAt** tự động set bởi Hibernate
4. **Quan hệ (relationships)** không cần dùng `.add()` - chỉ cần set object
5. **Lazy Loading**: Khi lấy user, các quan hệ (generatedCvs, comments...) chỉ load khi truy cập

---

## 🎓 Tóm Tắt

| Cách | Khi nào dùng | Ưu điểm |
|------|-------------|---------|
| **UserService** | Luôn dùng | Tự động xử lý mọi thứ |
| **Repository** | Khi cần tùy chỉnh | Linh hoạt hơn |
| **REST API** | Từ frontend/client | Dễ tích hợp |

**Kết luận:** Dùng `UserService.createUser()` là cách đơn giản và an toàn nhất! ✅

