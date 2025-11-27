Render giao diện trước (Preview)

Khi user nhấn “Xem trước CV” → FE gọi API preview

BE tạo HTML/PDF tạm thời trong bộ nhớ → trả về FE

FE hiển thị trong iframe hoặc popup để user xem CV như thật

Đây là tương tác trực tiếp, user phải chờ BE render xong mới thấy preview

Thực hiện lưu vào backend (Save)

Khi user nhấn “Lưu” → BE không render trực tiếp nữa

BE gửi job vào RabbitMQ queue

Worker chạy background → render PDF chính thức → lưu vào storage/DB

FE có thể nhận thông báo “PDF đã sẵn sàng” hoặc tải xuống PDF

🔹 Tóm tắt
Bước	Thực hiện	Đặc điểm
Preview	Render tạm thời → trả FE	Chờ BE → mất vài giây, không lưu file thật
Lưu PDF	Gửi job → Worker background	FE nhận phản hồi nhanh, PDF lưu thật sau


1️⃣ JasperReports

Chức năng chính: tạo báo cáo, PDF, Excel, HTML… từ dữ liệu (DB hoặc JSON) theo template.

Nó render file trực tiếp, có thể xuất PDF đẹp, có chart, bảng, format phức tạp.

Thường dùng khi bạn muốn: xuất CV PDF, báo cáo tài chính, invoice…

2️⃣ RabbitMQ

Chức năng chính: message broker, quản lý hàng đợi công việc (job queue).

Giúp xử lý bất đồng bộ, tách producer/consumer, tránh backend bị treo.

Không tạo file PDF hay báo cáo, nó chỉ đẩy job cho worker làm việc.

3️⃣ Sự kết hợp

RabbitMQ + worker + JasperReports / OpenHTML2PDF:

FE nhấn “Tạo CV” → BE gửi job vào queue RabbitMQ

Worker nhận job → dùng JasperReports hoặc HTML2PDF render PDF → lưu storage

FE nhận thông báo hoặc tải PDF

RabbitMQ chỉ là cơ chế bất đồng bộ, còn JasperReports là công cụ render PDF.