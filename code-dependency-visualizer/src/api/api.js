// const API_BASE_URL = 'http://localhost:8080/api';

// export const analyzeProject = async (gitRepoUrl) => {
//   try {
//     const formData = new FormData();
//     formData.append('gitRepoUrl', gitRepoUrl);

//     const response = await fetch(`${API_BASE_URL}/projects/analyze`, {
//       method: 'POST',
//       body: formData,
//     });

//     if (!response.ok) {
//       const errorText = await response.text();
//       throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
//     }

//     const data = await response.json();
//     console.log('API response:', data); // For debugging

//     return data;
//   } catch (error) {
//     console.error('Error analyzing project:', error);
//     throw error;
//   }
// };

const API_BASE_URL = 'http://localhost:8080/api';

export const analyzeProject = async (gitRepoUrl) => {
  try {
    const formData = new FormData();
    formData.append('gitRepoUrl', gitRepoUrl);

    const response = await fetch(`${API_BASE_URL}/projects/analyze`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
    }

    const data = await response.json();
    console.log('API response:', data); // For debugging

    return data;
  } catch (error) {
    console.error('Error analyzing project:', error);
    throw error;
  }
};

// Helper function to find a class within the project data
export const findClassData = (projectData, className) => {
  for (const module of projectData.modules) {
    for (const pkg of module.packages) {
      const foundClass = pkg.classes.find(cls => cls.name === className);
      if (foundClass) return foundClass;
    }
  }
  return null;
};