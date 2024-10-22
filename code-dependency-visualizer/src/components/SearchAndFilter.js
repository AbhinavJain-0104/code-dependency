import React, { useState } from 'react';

const SearchAndFilter = ({ onSearch, onFilter }) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [filter, setFilter] = useState('all');

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
        onSearch(e.target.value);
    };

    const handleFilterChange = (e) => {
        setFilter(e.target.value);
        onFilter(e.target.value);
    };

    return (
        <div className="search-and-filter">
            <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={handleSearchChange}
            />
            <select value={filter} onChange={handleFilterChange}>
                <option value="all">All</option>
                <option value="module">Modules</option>
                <option value="package">Packages</option>
                <option value="class">Classes</option>
            </select>
        </div>
    );
};

export default SearchAndFilter;