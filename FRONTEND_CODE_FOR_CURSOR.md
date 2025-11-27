# 🎨 Frontend Code - Copy vào Cursor để làm FE

## 📋 Hướng dẫn sử dụng

1. Mở Cursor
2. Copy từng phần code bên dưới
3. Paste vào Cursor prompt với yêu cầu: "Tạo component này cho tôi"
4. Cursor sẽ tự động tạo file và tích hợp vào project

---

## 1️⃣ Trang Xác thực Email (`/xac-thuc-email`)

### Prompt cho Cursor:
```
Tạo component React cho trang xác thực email với các yêu cầu:
- Route: /xac-thuc-email
- Lấy token từ query parameter (?token=xxx)
- Gọi API GET http://localhost:8000/api/auth/verify-email?token={token}
- Hiển thị loading khi đang verify
- Hiển thị thông báo thành công/thất bại
- Tự động redirect đến /dang-nhap sau 2 giây nếu thành công
- Xử lý các lỗi: token hết hạn, token không hợp lệ, email đã verify
```

### Code mẫu (React + TypeScript):

```tsx
// src/pages/EmailVerification.tsx hoặc src/pages/XacThucEmail.tsx
import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const EmailVerification = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    const token = searchParams.get('token');
    
    if (!token) {
      setError('Token xác thực không hợp lệ');
      setLoading(false);
      return;
    }

    // Gọi API verify email
    const verifyEmail = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8000/api/auth/verify-email?token=${token}`
        );
        
        if (response.data.verified === 'true') {
          setSuccess(true);
          setMessage(response.data.message || 'Xác thực email thành công!');
          
          // Redirect đến trang đăng nhập sau 2 giây
          setTimeout(() => {
            navigate('/dang-nhap');
          }, 2000);
        }
      } catch (err: any) {
        const errorMessage = err.response?.data?.message || 
                           err.message || 
                           'Không thể xác thực email. Vui lòng thử lại.';
        setError(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    verifyEmail();
  }, [searchParams, navigate]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
          <p className="mt-4 text-gray-600">Đang xác thực email...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8">
        {success ? (
          <div className="text-center">
            <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-green-100">
              <svg
                className="h-6 w-6 text-green-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M5 13l4 4L19 7"
                />
              </svg>
            </div>
            <h2 className="mt-4 text-xl font-semibold text-gray-900">
              Xác thực thành công!
            </h2>
            <p className="mt-2 text-sm text-gray-600">{message}</p>
            <p className="mt-4 text-sm text-gray-500">
              Đang chuyển đến trang đăng nhập...
            </p>
          </div>
        ) : (
          <div className="text-center">
            <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
              <svg
                className="h-6 w-6 text-red-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </div>
            <h2 className="mt-4 text-xl font-semibold text-gray-900">
              Xác thực thất bại
            </h2>
            <p className="mt-2 text-sm text-red-600">{error}</p>
            <div className="mt-6">
              <button
                onClick={() => navigate('/dang-ky')}
                className="text-blue-600 hover:text-blue-800 text-sm font-medium"
              >
                Đăng ký lại
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default EmailVerification;
```

### Thêm route (nếu dùng React Router):

```tsx
// src/App.tsx hoặc router file
import EmailVerification from './pages/EmailVerification';

// Thêm vào routes:
<Route path="/xac-thuc-email" element={<EmailVerification />} />
```

---

## 2️⃣ Cập nhật Trang Đăng Ký

### Prompt cho Cursor:
```
Cập nhật component đăng ký để:
- Sau khi đăng ký thành công, hiển thị thông báo "Vui lòng kiểm tra email để xác thực"
- KHÔNG tự động đăng nhập sau khi đăng ký
- Hiển thị email đã đăng ký trong thông báo
- Có thể redirect đến trang thông báo hoặc giữ nguyên trang với thông báo
```

### Code cập nhật (phần xử lý sau khi đăng ký):

```tsx
// Trong component đăng ký của bạn
const handleRegister = async (formData) => {
  try {
    const response = await axios.post(
      'http://localhost:8000/api/auth/register',
      formData
    );

    // Backend trả về: { message: "...", email: "..." }
    if (response.status === 200) {
      // Hiển thị thông báo thành công
      setSuccessMessage(
        response.data.message || 
        'Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.'
      );
      
      // Hiển thị email đã đăng ký
      setRegisteredEmail(response.data.email);
      
      // KHÔNG tự động đăng nhập
      // KHÔNG lưu token vào localStorage
      
      // Có thể redirect đến trang thông báo hoặc giữ nguyên
      // navigate('/thong-bao-dang-ky');
    }
  } catch (error: any) {
    const errorMessage = error.response?.data?.message || 
                        'Đăng ký thất bại. Vui lòng thử lại.';
    setErrorMessage(errorMessage);
  }
};

// UI hiển thị thông báo:
{successMessage && (
  <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
    <div className="flex">
      <div className="flex-shrink-0">
        <svg className="h-5 w-5 text-blue-400" viewBox="0 0 20 20" fill="currentColor">
          <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
        </svg>
      </div>
      <div className="ml-3">
        <p className="text-sm text-blue-700">{successMessage}</p>
        {registeredEmail && (
          <p className="text-sm text-blue-600 mt-1">
            Email: <strong>{registeredEmail}</strong>
          </p>
        )}
        <p className="text-sm text-blue-600 mt-2">
          Vui lòng kiểm tra hộp thư đến và spam để tìm email xác thực.
        </p>
      </div>
    </div>
  </div>
)}
```

---

## 3️⃣ Cập nhật Trang Đăng Nhập

### Prompt cho Cursor:
```
Cập nhật component đăng nhập để xử lý lỗi "Email chưa được xác thực":
- Khi nhận lỗi này từ API, hiển thị thông báo cảnh báo rõ ràng
- Hướng dẫn user kiểm tra email và click link xác thực
- Có thể thêm nút "Gửi lại email xác thực" (nếu có chức năng này)
```

### Code cập nhật (phần xử lý lỗi):

```tsx
// Trong component đăng nhập của bạn
const handleLogin = async (formData) => {
  try {
    const response = await axios.post(
      'http://localhost:8000/api/auth/login',
      formData
    );

    // Đăng nhập thành công
    if (response.data.accessToken) {
      localStorage.setItem('token', response.data.accessToken);
      navigate('/trang-chu');
    }
  } catch (error: any) {
    const errorMessage = error.response?.data?.message || 
                        'Đăng nhập thất bại. Vui lòng thử lại.';
    
    // Kiểm tra nếu là lỗi email chưa xác thực
    if (errorMessage.includes('Email chưa được xác thực') || 
        errorMessage.includes('chưa được xác thực')) {
      setEmailNotVerified(true);
      setErrorMessage(
        'Email của bạn chưa được xác thực. ' +
        'Vui lòng kiểm tra email và click vào link xác thực để kích hoạt tài khoản.'
      );
    } else {
      setErrorMessage(errorMessage);
    }
  }
};

// UI hiển thị cảnh báo email chưa verify:
{emailNotVerified && (
  <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-4">
    <div className="flex">
      <div className="flex-shrink-0">
        <svg className="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
          <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
        </svg>
      </div>
      <div className="ml-3">
        <h3 className="text-sm font-medium text-yellow-800">
          Email chưa được xác thực
        </h3>
        <div className="mt-2 text-sm text-yellow-700">
          <p>{errorMessage}</p>
          <p className="mt-2">
            Nếu không thấy email, vui lòng kiểm tra thư mục <strong>Spam</strong> hoặc <strong>Thư rác</strong>.
          </p>
        </div>
        {/* Có thể thêm nút "Gửi lại email" nếu có API này */}
        {/* <button onClick={handleResendEmail} className="mt-2 text-sm text-yellow-800 underline">
          Gửi lại email xác thực
        </button> */}
      </div>
    </div>
  </div>
)}
```

---

## 4️⃣ Tổng hợp - Copy tất cả vào Cursor

### Prompt đầy đủ cho Cursor:

```
Tôi cần tích hợp chức năng xác thực email đăng ký vào ứng dụng React của tôi. 
Backend đã sẵn sàng với các API:
- POST /api/auth/register - trả về { message, email }
- GET /api/auth/verify-email?token=xxx - xác thực email
- POST /api/auth/login - trả về lỗi nếu email chưa verify

Yêu cầu:
1. Tạo trang /xac-thuc-email để xử lý xác thực email từ link trong email
2. Cập nhật trang đăng ký: sau khi đăng ký thành công, hiển thị thông báo "Vui lòng kiểm tra email" và KHÔNG tự động đăng nhập
3. Cập nhật trang đăng nhập: xử lý lỗi "Email chưa được xác thực" với thông báo cảnh báo rõ ràng

Sử dụng:
- React Router cho routing
- Axios cho API calls
- Tailwind CSS cho styling (hoặc CSS framework bạn đang dùng)
- TypeScript (nếu có)

Tạo các component và cập nhật code hiện có theo yêu cầu trên.
```

---

## 📝 Checklist sau khi Cursor tạo code

- [ ] Trang `/xac-thuc-email` đã được tạo và thêm vào routes
- [ ] Trang đăng ký đã cập nhật: không tự động đăng nhập sau khi đăng ký
- [ ] Trang đăng nhập đã cập nhật: xử lý lỗi email chưa verify
- [ ] Test: Đăng ký → Kiểm tra email → Click link → Verify thành công → Redirect đến đăng nhập
- [ ] Test: Đăng nhập với email chưa verify → Hiển thị thông báo cảnh báo

---

## 🔗 API Endpoints cần dùng

```javascript
// 1. Đăng ký
POST http://localhost:8000/api/auth/register
Body: { email, password, fullName, phone }
Response: { message: "...", email: "..." }

// 2. Xác thực email
GET http://localhost:8000/api/auth/verify-email?token=xxx
Response: { message: "...", verified: "true" }

// 3. Đăng nhập
POST http://localhost:8000/api/auth/login
Body: { email, password }
Response: { accessToken, refreshToken, user, ... }
Error (nếu chưa verify): { message: "Email chưa được xác thực..." }
```

---

## 💡 Tips

1. **Copy từng phần**: Copy từng prompt vào Cursor, không copy tất cả cùng lúc
2. **Kiểm tra routes**: Đảm bảo route `/xac-thuc-email` đã được thêm vào router
3. **Test từng bước**: Test đăng ký → email → verify → đăng nhập
4. **Cập nhật API URL**: Nếu backend chạy ở port khác, cập nhật URL trong code

---

**Chúc bạn code vui vẻ! 🚀**

