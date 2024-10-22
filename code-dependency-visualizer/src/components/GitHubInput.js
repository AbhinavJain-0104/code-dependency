import React, { useState } from 'react';
import './GitHubInput.css';

const GitHubInput = ({ onSubmit }) => {
  const [url, setUrl] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!url) {
      setError('Please enter a GitHub URL');
      return;
    }

    try {
      await onSubmit(url);
    } catch (err) {
      setError(`Error: ${err.message}`);
    }
  };

  return (
    <div className="github-input">
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Enter GitHub URL"
          className="github-url-input"
        />
        <button type="submit" className="analyze-button">Analyze</button>
      </form>
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default GitHubInput;