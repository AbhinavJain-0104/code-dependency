import React from 'react';

const Legend = () => {
    return (
        <div style={{ position: 'absolute', bottom: 10, left: 10, background: 'rgba(255,255,255,0.7)', padding: 10 }}>
            <div><span style={{ color: '#4CAF50' }}>■</span> Module</div>
            <div><span style={{ color: '#2196F3' }}>■</span> Package</div>
            <div><span style={{ color: '#9C27B0' }}>■</span> Class</div>
        </div>
    );
};

export default Legend;