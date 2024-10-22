import React from 'react';
import './ProjectOverview.css';

const ProjectOverview = ({ projectData }) => {
  return (
    <div className="project-overview">
      <h2>Project Overview</h2>
      <p><strong>Name:</strong> {projectData.name}</p>
      <p><strong>Primary Language:</strong> Java</p>
      <p><strong>Total Classes:</strong> {projectData.totalClasses}</p>
      <p><strong>Total Methods:</strong> {projectData.totalMethods}</p>
    </div>
  );
};

export default ProjectOverview;