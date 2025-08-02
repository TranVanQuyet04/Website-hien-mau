/* === src/components/RegisterProgress.jsx === */
import React from "react";
import { Steps } from "antd";

const RegisterProgress = ({ currentStep, steps, icons }) => {
  const items = steps.map((step, index) => ({
    title: step,
    icon: icons[index],
    status: index < currentStep ? 'finish' : index === currentStep ? 'process' : 'wait'
  }));

  return (
    <div style={{ marginBottom: 32 }}>
      <Steps 
        current={currentStep}
        items={items}
        size="small"
        responsive={true}
        style={{
          marginBottom: 16
        }}
      />
    </div>
  );
};

export default RegisterProgress;