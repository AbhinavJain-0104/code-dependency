import React from 'react';
import './ClassInfo.css';

const ClassInfo = ({ classData }) => {
  return (
    <div className="class-info">
      <section className="general-info">
        <h2>General Information</h2>
        <p><strong>Package:</strong> {classData.packageName}</p>
        <p><strong>File Path:</strong> {classData.filePath}</p>
      </section>

      <section className="ai-description">
        <h2>AI Description</h2>
        <p>{classData.aiDescription}</p>
      </section>

      <section className="methods">
        <h2>Methods</h2>
        <ul>
          {classData.methods && classData.methods.map((method, index) => (
            <li key={index}>{method}</li>
          ))}
        </ul>
      </section>

      <section className="fields">
        <h2>Fields</h2>
        <ul>
          {classData.fields && classData.fields.map((field, index) => (
            <li key={index}>{field}</li>
          ))}
        </ul>
      </section>

      {classData.innerClasses && classData.innerClasses.length > 0 && (
        <section className="inner-classes">
          <h2>Inner Classes</h2>
          <ul>
            {classData.innerClasses.map((innerClass, index) => (
              <li key={index}>{innerClass}</li>
            ))}
          </ul>
        </section>
      )}
    </div>
  );
};

export default ClassInfo;