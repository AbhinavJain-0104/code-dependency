import React from 'react';
import { motion } from 'framer-motion';
import { FaTimes } from 'react-icons/fa';

const ClassInfoModal = ({ classData, onClose }) => {
  return (
    <motion.div 
      className="modal-overlay"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      <motion.div 
        className="class-info-modal"
        initial={{ opacity: 0, scale: 0.8 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.8 }}
      >
        <button className="close-button" onClick={onClose}>
          <FaTimes />
        </button>
        <h2>{classData.name}</h2>
        <p><strong>Package:</strong> {classData.packageName}</p>
        <p><strong>Description:</strong> {classData.aiDescription}</p>
        <div className="metrics">
          <div className="metric">
            <span className="metric-value">{classData.metrics.methodCount}</span>
            <span className="metric-label">Methods</span>
          </div>
          <div className="metric">
            <span className="metric-value">{classData.metrics.cyclomaticComplexity}</span>
            <span className="metric-label">Complexity</span>
          </div>
          <div className="metric">
            <span className="metric-value">{classData.metrics.linesOfCode}</span>
            <span className="metric-label">Lines</span>
          </div>
        </div>
        <details>
          <summary>Methods ({classData.methods.length})</summary>
          <ul>
            {classData.methods.map((method, index) => (
              <li key={index}>{method}</li>
            ))}
          </ul>
        </details>
        <details>
          <summary>Fields ({classData.fields.length})</summary>
          <ul>
            {classData.fields.map((field, index) => (
              <li key={index}>{field}</li>
            ))}
          </ul>
        </details>
      </motion.div>
    </motion.div>
  );
};

export default ClassInfoModal;