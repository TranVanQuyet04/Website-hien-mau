import React, { useState } from "react";
import { Card, Typography, Button, message } from "antd";

const { Title, Text } = Typography;

const questions = [
  { question: "Nhóm máu nào hiến được cho tất cả?", options: ["A-", "O-", "AB+", "B-"], answer: "O-" },
  { question: "Nhóm máu nào nhận được tất cả?", options: ["O+", "A+", "AB+", "O-"], answer: "AB+" },
  { question: "Nhóm máu nào hiếm nhất?", options: ["A-", "B-", "AB-", "O-"], answer: "AB-" },
  { question: "Nhóm máu nào phổ biến nhất ở Việt Nam?", options: ["O+", "A+", "B+", "AB+"], answer: "O+" },
  { question: "Người có nhóm máu AB- nhận được từ?", options: ["Tất cả", "O-", "A-", "AB-, A-, B-, O-"], answer: "AB-, A-, B-, O-" },
  { question: "Người có nhóm máu O- có thể nhận từ?", options: ["O+", "O-", "Tất cả", "A-"], answer: "O-" },
  { question: "Nhóm máu nào chỉ có thể hiến cho cùng nhóm?", options: ["O-", "AB+", "A-", "B-"], answer: "AB+" },
  { question: "Rh là gì trong nhóm máu?", options: ["Loại tế bào", "Yếu tố di truyền", "Kháng nguyên", "Protein"], answer: "Kháng nguyên" },
  { question: "Người có nhóm máu B+ có thể nhận từ?", options: ["B+, B-, O+, O-", "Tất cả", "O+", "B-"], answer: "B+, B-, O+, O-" },
  { question: "A- có thể hiến cho nhóm nào?", options: ["A+, A-, AB+, AB-", "Tất cả", "O-", "A-"], answer: "A+, A-, AB+, AB-" },
  { question: "O+ có thể hiến cho ai?", options: ["O+", "O-, A+, B+", "Rh+ nhóm", "Tất cả"], answer: "Rh+ nhóm" },
  { question: "AB+ thường được dùng cho?", options: ["Cấp cứu", "Phẫu thuật", "Truyền máu đặc biệt", "Tất cả"], answer: "Tất cả" },
  { question: "Ai nên hiến máu định kỳ nhất?", options: ["Người AB+", "Người O-", "Người A+", "Người AB-"], answer: "Người O-" },
  { question: "Tại sao nhóm máu hiếm cần bảo quản tốt?", options: ["Vì dễ đông", "Vì ít người có", "Vì không thể tái tạo", "Vì đắt tiền"], answer: "Vì ít người có" },
  { question: "Người có nhóm máu nào dễ gặp khó khi cần truyền khẩn?", options: ["O+", "B-", "AB-", "A+"], answer: "AB-" }
];

const BloodTypeQuiz = () => {
  const [index, setIndex] = useState(0);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  const handleAnswer = (selected) => {
    const correct = selected === questions[index].answer;
    if (correct) {
      message.success("✅ Chính xác!");
      setScore((prev) => prev + 1);
    } else {
      message.error(`❌ Sai rồi! Đáp án đúng là: ${questions[index].answer}`);
    }

    setTimeout(() => {
      if (index < questions.length - 1) {
        setIndex((prev) => prev + 1);
      } else {
        setFinished(true);
      }
    }, 1000);
  };

  const resetQuiz = () => {
    setIndex(0);
    setScore(0);
    setFinished(false);
  };

  return (
    <Card
      title={<Title level={4} style={{ color: "#cf1322", textAlign: "center" }}>🧠 Kiểm tra kiến thức về nhóm máu</Title>}
      style={{ marginTop: 32, borderRadius: 12 }}
    >
      {!finished ? (
        <div style={{ textAlign: "center" }}>
          <Title level={5} style={{ marginBottom: 16 }}>
            Câu {index + 1} / {questions.length}: {questions[index].question}
          </Title>
          {questions[index].options.map((opt) => (
            <Button
              key={opt}
              style={{ margin: "6px 8px", fontWeight: "bold", minWidth: 100 }}
              onClick={() => handleAnswer(opt)}
            >
              {opt}
            </Button>
          ))}
        </div>
      ) : (
        <div style={{ textAlign: "center" }}>
          <Title level={4} style={{ color: "#52c41a" }}>🎉 Bạn đã hoàn thành bài quiz!</Title>
          <Text strong style={{ fontSize: 16 }}>
            Kết quả: <span style={{ color: "#fa541c" }}>{score} / {questions.length} đúng</span>
          </Text>
          <br /><br />
          <Button type="primary" onClick={resetQuiz}>
            🔁 Làm lại từ đầu
          </Button>
        </div>
      )}
    </Card>
  );
};

export default BloodTypeQuiz;