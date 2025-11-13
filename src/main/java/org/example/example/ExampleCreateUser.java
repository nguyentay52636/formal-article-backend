package org.example.example;

import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Ví dụ đơn giản: Cách tạo User mới
 * 
 * File này chỉ để tham khảo, bạn có thể xóa sau khi đã hiểu cách dùng.
 * 
 * Để chạy ví dụ này, uncomment @Component bên dưới
 */
// @Component  // Bỏ comment này để chạy tự động khi start ứng dụng
public class ExampleCreateUser implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Ví dụ tạo User mới ===");
        
        try {
            // CÁCH 1: Dùng Service (KHUYẾN NGHỊ)
            User user1 = userService.createUser(
                "example@test.com",
                "password123",
                "Nguyễn Văn Example"
            );
            System.out.println("✅ User đã được tạo:");
            System.out.println("   ID: " + user1.getId());
            System.out.println("   Email: " + user1.getEmail());
            System.out.println("   Full Name: " + user1.getFullName());
            System.out.println("   Active: " + user1.getActive());
            System.out.println("   Created At: " + user1.getCreatedAt());
            
        } catch (RuntimeException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            System.out.println("   (Có thể email đã tồn tại)");
        }
        
        // CÁCH 2: Tạo User object rồi truyền vào Service
        try {
            User user2 = new User();
            user2.setEmail("example2@test.com");
            user2.setPassword("password123");  // Service sẽ tự mã hóa
            user2.setFullName("Trần Thị Example");
            
            User savedUser2 = userService.createUser(user2);
            System.out.println("\n✅ User 2 đã được tạo với ID: " + savedUser2.getId());
            
        } catch (RuntimeException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
        
        System.out.println("\n=== Kết thúc ví dụ ===");
    }
}

