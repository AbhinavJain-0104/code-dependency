// import React, { useState, useCallback } from 'react';
// import GitHubInput from './components/GitHubInput';
// import LoadingAnimation from './components/LoadingAnimation';
// import Visualization from './components/Visualization';
// import ClassDetailView from './components/ClassDetailView';
// import ErrorMessage from './components/ErrorMessage';
// import ProjectDetails from './components/ProjectDetails';
// import { analyzeProject } from './api/api';
// import './App.css';

// function App() {
//   const [isLoading, setIsLoading] = useState(false);
//   const [projectData, setProjectData] = useState(null);
//   const [selectedClass, setSelectedClass] = useState(null);
//   const [error, setError] = useState(null);
//   const [showDetails, setShowDetails] = useState(false);

//   const handleSubmit = useCallback(async (url) => {
//     setIsLoading(true);
//     setProjectData(null);
//     setSelectedClass(null);
//     setError(null);
//     try {
//       const result = await analyzeProject(url);
//       console.log('API Response:', result); // For debugging
//       setProjectData(result);
//     } catch (err) {
//       console.error('Error:', err); // For debugging
//       setError(`Error analyzing project: ${err.message}`);
//     } finally {
//       setIsLoading(false);
//     }
//   }, []);

//   const handleClassSelect = useCallback((classData) => {
//     setSelectedClass(classData);
//   }, []);

//   const handleBackToVisualization = useCallback(() => {
//     setSelectedClass(null);
//   }, []);

//   const toggleDetails = useCallback(() => {
//     setShowDetails(prev => !prev);
//   }, []);

//   return (
//     <div className="App">
//       <header className="App-header">
//         <h1>Code Dependency Visualizer</h1>
//       </header>
//       <main className="App-main">
//         <div className="search-container">
//           <GitHubInput onSubmit={handleSubmit} />
//         </div>
//         {isLoading && <LoadingAnimation />}
//         {error && <ErrorMessage message={error} />}
//          {projectData && (
//         <>
//           <button className="toggle-details" onClick={toggleDetails}>
//             {showDetails ? 'Hide Details' : 'Show Details'}
//           </button>
//           <div className="content-container">
//             {showDetails && <ProjectDetails projectData={projectData} />}
//             {!selectedClass && (
//               <div className="visualization-container">
//                 <Visualization 
//                   projectData={projectData} 
//                   onClassSelect={handleClassSelect}
//                 />
//               </div>
//             )}
//              {selectedClass && (
//               <ClassDetailView 
//                 classData={selectedClass}
//                 onBack={handleBackToVisualization}
//               />
//             )}
//           </div>
//         </>
//       )}
//       </main>
//     </div>
//   );
// }

// export default App;



import React, { useState, useCallback } from 'react';
import GitHubInput from './components/GitHubInput';
import LoadingAnimation from './components/LoadingAnimation';
import Visualization from './components/Visualization';
import ClassDetailPage from './components/ClassDetailPage';
import ErrorMessage from './components/ErrorMessage';
import ProjectDetails from './components/ProjectDetails';
import { analyzeProject, findClassData } from './api/api';
import './App.css';

function App() {
  const [isLoading, setIsLoading] = useState(false);
  const [projectData, setProjectData] = useState(null);
  const [selectedClass, setSelectedClass] = useState(null);
  const [error, setError] = useState(null);
  const [showDetails, setShowDetails] = useState(false);

  const handleSubmit = useCallback(async (url) => {
    setIsLoading(true);
    setProjectData(null);
    setSelectedClass(null);
    setError(null);
    try {
      const result = await analyzeProject(url);
      setProjectData(result);
    } catch (err) {
      console.error('Error:', err);
      setError(`Error analyzing project: ${err.message}`);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const handleClassSelect = useCallback((classData) => {
    setSelectedClass(classData);
  }, []);

  const handleBackToVisualization = useCallback(() => {
    setSelectedClass(null);
  }, []);

  const toggleDetails = useCallback(() => {
    setShowDetails(prev => !prev);
  }, []);

  const fetchClassData = useCallback((className) => {
    return findClassData(projectData, className);
  }, [projectData]);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Code Dependency Visualizer</h1>
      </header>
      <main className="App-main">
        <div className="search-container">
          <GitHubInput onSubmit={handleSubmit} />
        </div>
        {isLoading && <LoadingAnimation />}
        {error && <ErrorMessage message={error} />}
        {projectData && (
          <>
            <button className="toggle-details" onClick={toggleDetails}>
              {showDetails ? 'Hide Details' : 'Show Details'}
            </button>
            <div className="content-container">
              {showDetails && <ProjectDetails projectData={projectData} />}
              {!selectedClass && (
                <div className="visualization-container">
                  <Visualization 
                    projectData={projectData} 
                    onClassSelect={handleClassSelect}
                  />
                </div>
              )}
              {selectedClass && (
                <ClassDetailPage 
                  initialClassData={selectedClass}
                  onBack={handleBackToVisualization}
                  fetchClassData={fetchClassData}
                />
              )}
            </div>
          </>
        )}
      </main>
    </div>
  );
}

export default App;