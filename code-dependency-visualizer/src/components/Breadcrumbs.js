import React from 'react';

const Breadcrumbs = ({ history, onNavigate }) => {
    return (
        <div className="breadcrumbs">
            {history.map((item, index) => (
                <React.Fragment key={index}>
                    <span 
                        className="breadcrumb-item" 
                        onClick={() => onNavigate(item)}
                    >
                        {item.name}
                    </span>
                    {index < history.length - 1 && (
                        <span className="breadcrumb-separator">/</span>
                    )}
                </React.Fragment>
            ))}
        </div>
    );
};

export default Breadcrumbs;