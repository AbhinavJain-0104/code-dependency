import React, { useState } from 'react';

const GitHubInput = ({ onAnalyze }) => {
    const [gitRepoUrl, setGitRepoUrl] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (isSubmitting) return;

        setIsSubmitting(true);

        try {
            await onAnalyze(gitRepoUrl);
        } catch (err) {
            setError('Error analyzing the repository. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="GitHub Repository URL"
                value={gitRepoUrl}
                onChange={(e) => setGitRepoUrl(e.target.value)}
                required
            />
            <button type="submit" disabled={isSubmitting}>Analyze</button>
            {error && <p>{error}</p>}
        </form>
    );
};

export default GitHubInput;