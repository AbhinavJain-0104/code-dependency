import React from 'react';
import './ProjectDetails.css';

const ProjectDetails = ({ projectData }) => {
  const totalClasses = projectData && projectData.modules ? 
    projectData.modules.reduce((acc, module) => 
      acc + (module.packages ? module.packages.reduce((pkgAcc, pkg) => 
        pkgAcc + (pkg.classes ? pkg.classes.length : 0), 0) : 0
      ), 0
    ) : 0;

  return (
    <div className="project-details">
      <h2>{projectData && projectData.name ? projectData.name : 'Unnamed Project'}</h2>
      <p>Status: {projectData && projectData.status ? projectData.status : 'Unknown'}</p>
      <p>Total Modules: {projectData && projectData.modules ? projectData.modules.length : 0}</p>
      <p>Total Classes: {totalClasses}</p>
      <p>Path: {projectData && projectData.path ? projectData.path : 'Unknown'}</p>
    </div>
  );
};

export default ProjectDetails;