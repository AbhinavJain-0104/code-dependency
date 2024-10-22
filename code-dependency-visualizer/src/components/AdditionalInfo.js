import React, { useState } from 'react';
import './AdditionalInfo.css';

const AdditionalInfo = ({ projectData }) => {
  const [expandedSections, setExpandedSections] = useState({});

  const toggleSection = (section) => {
    setExpandedSections(prev => ({ ...prev, [section]: !prev[section] }));
  };

  return (
    <div className="additional-info">
      <h2>Additional Project Information</h2>
      <div className="info-section">
        <h3 onClick={() => toggleSection('apiUsage')}>API Usage Patterns</h3>
        {expandedSections.apiUsage && (
          <ul>
            {projectData.apiUsagePatterns && projectData.apiUsagePatterns.map((pattern, index) => (
              <li key={index}>{pattern.apiCall} - Used {pattern.usageCount} times</li>
            ))}
          </ul>
        )}
      </div>
      <div className="info-section">
        <h3 onClick={() => toggleSection('performanceInsights')}>Performance Insights</h3>
        {expandedSections.performanceInsights && (
          <ul>
            {projectData.performanceInsights && projectData.performanceInsights.map((insight, index) => (
              <li key={index}>{insight.description}</li>
            ))}
          </ul>
        )}
      </div>
      <div className="info-section">
        <h3 onClick={() => toggleSection('styleInconsistencies')}>Style Inconsistencies</h3>
        {expandedSections.styleInconsistencies && (
          <ul>
            {projectData.styleInconsistencies && projectData.styleInconsistencies.map((inconsistency, index) => (
              <li key={index}>{inconsistency.description} at {inconsistency.location}</li>
            ))}
          </ul>
        )}
      </div>
      <div className="info-section">
        <h3 onClick={() => toggleSection('versionIssues')}>Version Issues</h3>
        {expandedSections.versionIssues && (
          <ul>
            {projectData.versionIssues && projectData.versionIssues.map((issue, index) => (
              <li key={index}>{issue.dependencyName} - {issue.issue}</li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default AdditionalInfo;