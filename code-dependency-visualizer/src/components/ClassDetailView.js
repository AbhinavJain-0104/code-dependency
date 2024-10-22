import React, { useState, useCallback } from 'react';
import { ForceGraph2D } from 'react-force-graph';
import './ClassDetailView.css';

const ClassDetailView = ({ initialClassData, onBack, fetchClassData }) => {
  const [classStack, setClassStack] = useState([initialClassData]);
  const currentClassData = classStack[classStack.length - 1];

  const graphData = React.useMemo(() => {
    const nodes = [{ id: currentClassData.name, group: 'main' }];
    const links = [];

    currentClassData.dependencies.forEach(dep => {
      nodes.push({ id: dep, group: 'dependency' });
      links.push({ source: currentClassData.name, target: dep });
    });

    return { nodes, links };
  }, [currentClassData]);

  const handleNodeClick = useCallback(async (node) => {
    if (node.group === 'dependency') {
      const newClassData = await fetchClassData(node.id);
      if (newClassData) {
        setClassStack(prevStack => [...prevStack, newClassData]);
      }
    }
  }, [fetchClassData]);

  const handleBack = () => {
    if (classStack.length > 1) {
      setClassStack(prevStack => prevStack.slice(0, -1));
    } else {
      onBack();
    }
  };

  return (
    <div className="class-detail-view">
      <button onClick={handleBack}>Back</button>
      <h2>{currentClassData.name}</h2>
      <div className="class-info">
        <p><strong>Methods:</strong> {currentClassData.methods.join(', ')}</p>
        <p><strong>Fields:</strong> {currentClassData.fields.join(', ')}</p>
      </div>
      <div className="class-graph">
        <ForceGraph2D
          graphData={graphData}
          nodeAutoColorBy="group"
          nodeLabel={node => node.id}
          onNodeClick={handleNodeClick}
          width={600}
          height={400}
        />
      </div>
    </div>
  );
};

export default ClassDetailView;