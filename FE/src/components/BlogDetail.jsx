import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../styles/BlogList.css";
import { CalendarOutlined } from "@ant-design/icons";
import { Tooltip, Modal, Tag } from "antd";

const blogs = [
  {
    id: "1",
    title: "Lý do khiến hiến máu trở thành nghĩa cử cao đẹp",
    summary: "Mỗi giọt máu bạn trao đi là một tia hy vọng cho người khác. Hiến máu không chỉ cứu người mà còn lan tỏa yêu thương và sự sống trong cộng đồng.",
    published_at: "2025-06-03",
    image: "/blog1.jpg",
    category: "Truyền cảm hứng"
  },
  {
    id: "2",
    title: "Chuẩn bị đúng cách để hiến máu nhẹ nhàng hơn",
    summary: "Chỉ với vài bước đơn giản như nghỉ ngơi đủ giấc, ăn uống hợp lý và giữ tinh thần lạc quan, bạn có thể trải nghiệm buổi hiến máu thoải mái và suôn sẻ.",
    published_at: "2025-06-01",
    image: "/blog2.jpg",
    category: "Hướng dẫn"
  },
  {
    id: "3",
    title: "Hiến máu – Trao đi sức khỏe, nhận lại yêu thương",
    summary: "Không chỉ là sự sẻ chia, hiến máu còn mang lại lợi ích sức khỏe như tái tạo máu, phát hiện sớm bệnh lý và cảm giác mãn nguyện khi giúp đỡ người khác.",
    published_at: "2025-06-03",
    image: "/blog3.jpg",
    category: "Truyền cảm hứng"
  },
  {
    id: "4",
    title: "Điều kiện để trở thành người hiến máu",
    summary: "Chỉ cần đủ 18 tuổi, sức khỏe tốt và đủ cân nặng là bạn đã có thể trở thành một người hùng thầm lặng qua việc hiến máu tình nguyện.",
    published_at: "2025-06-02",
    image: "/blog4.jpg",
    category: "Thông tin"
  },
  {
    id: "5",
    title: "Toàn bộ quy trình hiến máu an toàn và chuyên nghiệp",
    summary: "Từ khâu đăng ký đến chăm sóc sau hiến, mọi bước đều được thực hiện cẩn trọng, đảm bảo sự an tâm tuyệt đối cho mỗi người tham gia hiến máu.",
    published_at: "2025-06-01",
    image: "/blog5.jpg",
    category: "Quy trình"
  }
];

const formatDate = (dateStr) => {
  const options = { day: "2-digit", month: "2-digit", year: "numeric" };
  return new Date(dateStr).toLocaleDateString("vi-VN", options);
};

const BlogList = () => {
  const [selectedBlog, setSelectedBlog] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const handleOpenModal = (blog) => {
    setSelectedBlog(blog);
    setIsModalVisible(true);
  };

  const handleCloseModal = () => {
    setIsModalVisible(false);
    setSelectedBlog(null);
  };

  const sortedBlogs = [...blogs].sort(
    (a, b) => new Date(b.published_at) - new Date(a.published_at)
  );

  return (
    <div className="blog-list-wrapper">
      <h2>Bài viết nổi bật</h2>
      <div className="blog-list-grid">
        {sortedBlogs.map((blog) => (
          <div className="blog-list-card" key={blog.id}>
            <div className="clickable-area" onClick={() => handleOpenModal(blog)}>
              <img src={blog.image} alt={blog.title} className="blog-thumbnail" />
            </div>
            <Link to={`/blog/${blog.id}`} className="title-link">
              <h4>{blog.title}</h4>
            </Link>
            <div className="clickable-area" onClick={() => handleOpenModal(blog)}>
              <p>{blog.summary}</p>
            </div>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <Tooltip title="Ngày đăng bài">
                <small>
                  <CalendarOutlined style={{ marginRight: "6px" }} />
                  {formatDate(blog.published_at)}
                </small>
              </Tooltip>
              <Tag color="red">{blog.category}</Tag>
            </div>
          </div>
        ))}
      </div>

      {/* Modal chi tiết */}
      <Modal
        open={isModalVisible}
        title={selectedBlog?.title}
        onCancel={handleCloseModal}
        footer={null}
        width={700}
      >
        {selectedBlog && (
          <>
            <img
              src={selectedBlog.image}
              alt={selectedBlog.title}
              style={{ width: "100%", borderRadius: 10, marginBottom: 16 }}
            />
            <p>{selectedBlog.summary}</p>
            <p>
              📅 Ngày đăng: <strong>{formatDate(selectedBlog.published_at)}</strong>
            </p>
            <Tag color="volcano">📚 {selectedBlog.category}</Tag>
          </>
        )}
      </Modal>
    </div>
  );
};

export default BlogList;