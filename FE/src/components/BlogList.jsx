import React from "react";
import { Link } from "react-router-dom";
import "../styles/BlogList.css";

const blogs = [
  {
    id: "1",
    title: "Lý do khiến hiến máu trở thành nghĩa cử cao đẹp",
    summary: "Mỗi giọt máu bạn trao đi là một tia hy vọng cho người khác. Hiến máu không chỉ cứu người mà còn lan tỏa yêu thương và sự sống trong cộng đồng.",
    published_at: "2025-06-03",
    image: "/blog1.jpg"
  },
  {
    id: "2",
    title: "Chuẩn bị đúng cách để hiến máu nhẹ nhàng hơn",
    summary: "Chỉ với vài bước đơn giản như nghỉ ngơi đủ giấc, ăn uống hợp lý và giữ tinh thần lạc quan, bạn có thể trải nghiệm buổi hiến máu thoải mái và suôn sẻ.",
    published_at: "2025-06-01",
    image: "/blog2.jpg"
  },
  {
    id: "3",
    title: "Hiến máu – Trao đi sức khỏe, nhận lại yêu thương",
    summary: "Không chỉ là sự sẻ chia, hiến máu còn mang lại lợi ích sức khỏe như tái tạo máu, phát hiện sớm bệnh lý và cảm giác mãn nguyện khi giúp đỡ người khác.",
    published_at: "2025-06-03",
    image: "/blog3.jpg"
  },
  {
    id: "4",
    title: "Điều kiện để trở thành người hiến máu",
    summary: "Chỉ cần đủ 18 tuổi, sức khỏe tốt và đủ cân nặng là bạn đã có thể trở thành một người hùng thầm lặng qua việc hiến máu tình nguyện.",
    published_at: "2025-06-02",
    image: "/blog4.jpg"
  },
  {
    id: "5",
    title: "Toàn bộ quy trình hiến máu an toàn và chuyên nghiệp",
    summary: "Từ khâu đăng ký đến chăm sóc sau hiến, mọi bước đều được thực hiện cẩn trọng, đảm bảo sự an tâm tuyệt đối cho mỗi người tham gia hiến máu.",
    published_at: "2025-06-01",
    image: "/blog5.jpg"
  }
];

const formatDate = (dateStr) => {
  const options = { day: "2-digit", month: "2-digit", year: "numeric" };
  return new Date(dateStr).toLocaleDateString("vi-VN", options);
};

const BlogList = () => {
  const sortedBlogs = [...blogs].sort(
    (a, b) => new Date(b.published_at) - new Date(a.published_at)
  );

  return (
    <div className="blog-list-wrapper">
      <h2>Bài viết nổi bật</h2>
      <div className="blog-list-grid">
        {sortedBlogs.map((blog) => (
          <div className="blog-list-card" key={blog.id}>
            <Link to={`/blog/${blog.id}`} className="blog-link">
              <img src={blog.image} alt={blog.title} className="blog-thumbnail" />
              <h4>{blog.title}</h4>
              <p>{blog.summary}</p>
              <small>🗓️ {formatDate(blog.published_at)}</small>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BlogList;
