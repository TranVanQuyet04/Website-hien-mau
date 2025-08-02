import React, { useState } from "react";
import "../styles/BlogAccordion.css";

const blogData = [
    {
        title: "1. Ai có thể tham gia hiến máu?",
        content: [
            "Tất cả mọi người từ 18 - 60 tuổi, có sức khỏe tốt và tình nguyện hiến máu.",
            "Nam giới cần nặng tối thiểu 50kg, nữ giới từ 45kg trở lên.",
            "Người không mắc các bệnh truyền nhiễm như HIV, viêm gan B, giang mai,...",
            "Thời gian giữa hai lần hiến máu toàn phần tối thiểu là 12 tuần.",
            "Cần mang theo giấy tờ tùy thân như CMND/CCCD khi đi hiến máu."
        ]
    },
    {
        title: "2. Ai là người không nên hiến máu?",
        content: [
            "Người đang bị cảm cúm, sốt, ho, hoặc có vấn đề về đường hô hấp.",
            "Phụ nữ đang mang thai, cho con bú, hoặc trong kỳ kinh nguyệt.",
            "Người từng mắc bệnh mãn tính như tim mạch, tiểu đường, hen suyễn.",
            "Người đang dùng thuốc kháng sinh hoặc vừa phẫu thuật trong 6 tháng gần nhất.",
            "Nếu từng xăm hình dưới 6 tháng, cũng chưa nên hiến máu ngay."
        ]
    },
    {
        title: "3. Hiến máu có ảnh hưởng đến sức khỏe không?",
        content: [
            "Không. Lượng máu hiến ra sẽ được cơ thể tái tạo sau 3–5 ngày.",
            "Sau hiến máu, nên nghỉ ngơi 10–15 phút và uống nước cam, ăn nhẹ.",
            "Người hiến máu sẽ được kiểm tra huyết áp, nhịp tim, và xét nghiệm máu miễn phí.",
            "Hiến máu định kỳ còn giúp kiểm tra sức khỏe và phát hiện sớm bệnh lý."
        ]
    },
    {
        title: "4. Máu sau khi hiến được xử lý thế nào?",
        content: [
            "Máu sẽ được kiểm tra, sàng lọc kỹ lưỡng để loại trừ bệnh truyền nhiễm.",
            "Sau đó, máu sẽ được tách thành các thành phần như hồng cầu, tiểu cầu, huyết tương.",
            "Mỗi thành phần sẽ được bảo quản riêng và dùng cho từng bệnh nhân phù hợp.",
            "Toàn bộ quá trình đều tuân thủ quy trình nghiêm ngặt về y tế và an toàn sinh học."
        ]
    },
    {
        title: "5. Tôi cần chuẩn bị gì trước khi hiến máu?",
        content: [
            "Ngủ đủ giấc vào đêm hôm trước (ít nhất 6–8 tiếng).",
            "Ăn nhẹ trước khi đi hiến máu (tránh đồ chiên, béo, hoặc rượu bia).",
            "Không nên hiến máu nếu vừa tập thể dục cường độ cao.",
            "Mang theo giấy tờ tùy thân để đăng ký hiến máu."
        ]
    },
    {
        title: "6. Lợi ích của việc hiến máu là gì?",
        content: [
            "Hiến máu là hành động nhân văn, giúp cứu sống nhiều người bệnh.",
            "Cải thiện tuần hoàn máu, giúp cơ thể sản sinh máu mới khỏe mạnh hơn.",
            "Giảm nguy cơ mắc các bệnh tim mạch nếu hiến máu đều đặn.",
            "Người hiến máu còn được cấp giấy chứng nhận và hỗ trợ máu khi cần."
        ]
    },
    {
        title: "7. Khi nào không nên hiến máu?",
        content: [
            "Khi đang bị cảm cúm, sốt hoặc nhiễm trùng.",
            "Nếu đang dùng thuốc kháng sinh hoặc điều trị bệnh mãn tính.",
            "Phụ nữ đang mang thai hoặc mới sinh chưa đủ 6 tháng.",
            "Người vừa trải qua phẫu thuật hoặc tiêm chủng trong thời gian ngắn."
        ]
    },
    {
        title: "8. Sau khi hiến máu nên làm gì?",
        content: [
            "Nghỉ ngơi ít nhất 10–15 phút tại chỗ hiến máu.",
            "Uống nhiều nước, tránh vận động mạnh trong 24 giờ.",
            "Không nên sử dụng rượu bia và thức uống kích thích.",
            "Ăn thực phẩm giàu chất sắt như thịt đỏ, rau xanh đậm."
        ]
    },
    {
        title: "9. Bao lâu thì có thể hiến máu lại?",
        content: [
            "Khoảng cách tối thiểu giữa 2 lần hiến máu toàn phần là 12 tuần.",
            "Hiến tiểu cầu hoặc huyết tương thì có thể sau 4 tuần.",
            "Đảm bảo sức khỏe phục hồi đầy đủ trước khi đăng ký lại.",
            "Luôn tuân theo khuyến nghị của bác sĩ tại điểm hiến máu."
        ]
    },
    {
        title: "10. Có thể hiến máu khi đang uống thuốc không?",
        content: [
            "Tùy loại thuốc và tình trạng bệnh, nên thông báo cho nhân viên y tế.",
            "Một số thuốc thông thường có thể ảnh hưởng đến kết quả xét nghiệm.",
            "Không nên tự ý hiến máu nếu đang điều trị bệnh bằng thuốc kháng sinh mạnh.",
            "Luôn hỏi ý kiến bác sĩ trước khi quyết định hiến máu trong thời gian dùng thuốc."
        ]
    }

];

const BlogAccordion = () => {
    const [openIndex, setOpenIndex] = useState(null);

    const toggleAccordion = (index) => {
        setOpenIndex(index === openIndex ? null : index);
    };

    return (
        <div className="accordion-container">
            <h2 className="accordion-title">Lưu ý quan trọng</h2>
            {blogData.map((item, index) => (
                <div className="accordion-item" key={index}>
                    <div className="accordion-header" onClick={() => toggleAccordion(index)}>
                        {item.title}
                        <span>{openIndex === index ? "▲" : "▼"}</span>
                    </div>
                    {openIndex === index && (
                        <div className="accordion-body">
                            <ul>
                                {item.content.map((line, idx) => (
                                    <li key={idx}>{line}</li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
};

export default BlogAccordion;
