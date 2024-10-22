import React from 'react';
import './LoadingAnimation.css';

const LoadingAnimation = () => {
  return (
    <div className="loading-animation">
      <div className="spinner"></div>
      <p>Analyzing project...</p>
    </div>
  );
};

export default LoadingAnimation;