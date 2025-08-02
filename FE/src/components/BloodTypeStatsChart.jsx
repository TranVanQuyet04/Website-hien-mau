import React from "react";
import { Pie } from "react-chartjs-2";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  Title,
} from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend, Title);

const BloodTypeStatsChart = () => {
  const data = {
    labels: ["O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-"],
    datasets: [
      {
        label: "Tỷ lệ nhóm máu tại Việt Nam",
        data: [42, 20, 25, 7, 3, 1.5, 1, 0.5],
        backgroundColor: [
          "#ffcc00",
          "#ff9933",
          "#ff3366",
          "#ff66cc",
          "#00ccff",
          "#6699ff",
          "#33cc33",
          "#9966ff",
        ],
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "bottom",
      },
      title: {
        display: true,
        text: "\uD83D\uDCC8 Tỷ lệ nhóm máu tại Việt Nam (ước lượng)",
        font: {
          size: 18,
        },
        padding: {
          top: 20,
          bottom: 10,
        },
      },
    },
  };

  return (
    <div style={{ width: "100%", maxWidth: 600, margin: "0 auto", padding: 24 }}>
      <Pie data={data} options={options} />
    </div>
  );
};

export default BloodTypeStatsChart;