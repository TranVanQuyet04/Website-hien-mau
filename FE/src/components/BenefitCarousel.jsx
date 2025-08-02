import React, { useEffect, useState } from "react";
import "../styles/BenefitCarousel.css";

const benefitSlides = [
  {
    title: "Được bồi dưỡng trực tiếp",
    content: [
      "Sau khi hiến máu, bạn sẽ được mời ăn nhẹ tại chỗ để giúp cơ thể hồi phục nhanh hơn.",
      "Bạn cũng nhận được 50.000 đồng tiền mặt để hỗ trợ chi phí đi lại trong ngày hiến máu.",
      "Ngoài ra, bạn còn được tặng quà tuỳ theo lượng máu hiến: 100.000đ, 150.000đ hoặc 180.000đ."
    ]
  },
  {
    title: "Được khám và theo dõi sức khoẻ",
    content: [
      "Trước khi hiến, bạn sẽ được đo huyết áp, kiểm tra nhịp tim và tình trạng sức khoẻ tổng quát.",
      "Nếu có bất thường trong máu sau khi xét nghiệm, bạn sẽ được thông báo kịp thời.",
      "Bạn còn được cấp thẻ hiến máu để tiện theo dõi lịch sử và chăm sóc sức khoẻ định kỳ."
    ]
  },
  {
    title: "Được tư vấn về sức khoẻ",
    content: [
      "Nhân viên y tế sẽ hướng dẫn bạn về quá trình hiến máu và những điều cần lưu ý.",
      "Bạn sẽ được cung cấp kiến thức về các bệnh có thể lây qua máu và cách phòng tránh.",
      "Sau hiến máu, bạn cũng được tư vấn cách nghỉ ngơi, ăn uống để hồi phục nhanh chóng."
    ]
  }
];


const BenefitCarousel = () => {
  const [index, setIndex] = useState(0);

  const next = () => setIndex((prev) => (prev + 1) % benefitSlides.length);
  const prev = () => setIndex((prev) => (prev - 1 + benefitSlides.length) % benefitSlides.length);

  useEffect(() => {
    const interval = setInterval(next, 8000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="benefit-container">
      <div className="benefit-left">
        <div className="benefit-overlay">
          <h2>Quyền lợi của người hiến máu</h2>
          <p>Người hiến máu tình nguyện sẽ được những quyền lợi sau:</p>
        </div>
        <img src="/benefit.jpg" alt="Benefit Background" />
      </div>

      <div className="benefit-right">
        <div className="benefit-slide">
          <h3 className="benefit-title">{benefitSlides[index].title}</h3>
          <ul className="benefit-list">
            {benefitSlides[index].content.map((item, i) => (
              <li key={i}>{item}</li>
            ))}
          </ul>
        </div>

        <div className="benefit-arrows">
          <button onClick={prev}>&lt;</button>
          <button onClick={next}>&gt;</button>
        </div>
      </div>
    </div>
  );
};

export default BenefitCarousel;
