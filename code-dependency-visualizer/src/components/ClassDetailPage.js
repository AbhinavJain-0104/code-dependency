import React, { useState } from 'react';
import ClassGraph from './ClassGraph';
import ClassMetrics from './ClassMetrics';
import ClassInfo from './ClassInfo';
import './ClassDetailPage.css';

const ClassDetailPage = ({ initialClassData, onBack, fetchClassData }) => {
  const [classStack, setClassStack] = useState([initialClassData]);
  const currentClassData = classStack[classStack.length - 1];

  const handleClassClick = async (className) => {
    const newClassData = await fetchClassData(className);
    if (newClassData) {
      setClassStack(prevStack => [...prevStack, newClassData]);
    }
  };

  const handleBack = () => {
    if (classStack.length > 1) {
      setClassStack(prevStack => prevStack.slice(0, -1));
    } else {
      onBack();
    }
  };

  return (
    <div className="class-detail-page">
      <header className="class-detail-header">
        <button onClick={handleBack} className="back-button">Back</button>
        <h1>{currentClassData.name}</h1>
      </header>
      <div className="class-detail-content">
        <div className="left-panel">
          <ClassGraph 
            classData={currentClassData} 
            onClassClick={handleClassClick} 
          />
          <ClassMetrics metrics={currentClassData.metrics} />
        </div>
        <div className="right-panel">
          <ClassInfo classData={currentClassData} />
        </div>
      </div>
    </div>
  );
};

export default ClassDetailPage;