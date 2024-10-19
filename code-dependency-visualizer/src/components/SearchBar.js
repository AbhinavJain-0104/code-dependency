import React, { useState } from 'react';

const SearchBar = ({ onSearch }) => {
    const [term, setTerm] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSearch(term);
    };

    return (
        <form onSubmit={handleSubmit} style={{ marginBottom: '10px' }}>
            <input
                type="text"
                value={term}
                onChange={(e) => setTerm(e.target.value)}
                placeholder="Search for packages or classes"
                style={{ marginRight: '5px' }}
            />
            <button type="submit">Search</button>
        </form>
    );
};

export default SearchBar;