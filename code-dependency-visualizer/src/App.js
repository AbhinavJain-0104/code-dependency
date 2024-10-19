import React, { useState } from 'react';
import GitHubInput from './components/GitHubInput';
import Visualization from './components/Visualization';
import ProjectDetails from './components/ProjectDetails';
import ErrorMessage from './components/ErrorMessage';
import SearchBar from './components/SearchBar';
import { analyzeProject, setCurrentProjectData } from './api/api';
import './App.css';

const App = () => {
    const [projectData, setProjectData] = useState(null);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    const handleAnalyze = async (gitRepoUrl) => {
        try {
            const data = await analyzeProject(gitRepoUrl);
            if (data && data.modules && Array.isArray(data.modules)) {
                setProjectData(data);
                setCurrentProjectData(data);
                setError('');
            } else {
                throw new Error('Invalid data structure received from the server');
            }
        } catch (err) {
            setError(err.message);
            setProjectData(null);
        }
    };

    const handleSearch = (term) => {
        setSearchTerm(term);
    };

    return (
        <div className="App">
            <header className="header">
            <h1>Code Analyser</h1>
               

            </header>
            <div className="main-content">
                <div className="sidebar">
                    <GitHubInput onAnalyze={handleAnalyze} />
                    {error && <ErrorMessage message={error} />}
                    {projectData && <ProjectDetails />}
                    {projectData && <SearchBar onSearch={handleSearch} />}
                </div>
                <div className="visualization-container">
                    {projectData && <Visualization data={projectData} searchTerm={searchTerm} />}
                </div>
            </div>
        </div>
    );
};

export default App;