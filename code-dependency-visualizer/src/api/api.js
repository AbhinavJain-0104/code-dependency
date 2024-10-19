import axios from 'axios';

const API_URL = 'http://localhost:8080/api/projects';

const validateProjectData = (data) => {
    return data && 
           data.id !== undefined && 
           data.id !== null &&
           Array.isArray(data.modules) && 
           data.modules.every(module => Array.isArray(module.packages)) &&
           Array.isArray(data.dependencies);
};

export const analyzeProject = async (gitRepoUrl) => {
    try {
        const formData = new FormData();
        formData.append('gitRepoUrl', gitRepoUrl);

        const response = await axios.post(`${API_URL}/upload`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });

        console.log('API Response:', response.data);  // For debugging

        if (validateProjectData(response.data)) {
            return response.data;
        } else {
            throw new Error('Invalid data structure received from the server');
        }
    } catch (error) {
        console.error('Error:', error);
        throw new Error('Error analyzing the repository. Please try again.');
    }
};

export const getProjectDetails = async (projectId) => {
    try {
        const response = await axios.get(`${API_URL}/${projectId}`);
        return response.data;
    } catch (error) {
        throw new Error('Error fetching project details. Please try again.');
    }
};

let currentProjectData = null;

export const setCurrentProjectData = (data) => {
    currentProjectData = data;
};

export const getCurrentProjectData = () => {
    return currentProjectData;
};