import React from 'react';

const ContextMenu = ({ x, y, options, onSelect }) => {
    return (
        <div className="context-menu" style={{ top: y, left: x }}>
            {options.map((option, index) => (
                <div 
                    key={index} 
                    className="context-menu-item"
                    onClick={() => onSelect(option)}
                >
                    {option.label}
                </div>
            ))}
        </div>
    );
};

export default ContextMenu;