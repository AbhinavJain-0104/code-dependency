import React, { useCallback } from 'react';
import { ForceGraph2D } from 'react-force-graph';
import './ClassGraph.css';

const ClassGraph = ({ classData, onClassClick }) => {
  const graphData = React.useMemo(() => {
    const nodes = [{ id: classData.name, group: 'main' }];
    const links = [];

    if (classData.usedClasses) {
      classData.usedClasses.forEach(usedClass => {
        nodes.push({ id: usedClass, group: 'dependency' });
        links.push({ source: classData.name, target: usedClass });
      });
    }

    return { nodes, links };
  }, [classData]);

  const handleNodeClick = useCallback((node) => {
    if (node.group === 'dependency') {
      onClassClick(node.id);
    }
  }, [onClassClick]);

  return (
    <div className="class-graph">
      <h2>Class Dependencies</h2>
      <ForceGraph2D
        graphData={graphData}
        nodeAutoColorBy="group"
        nodeLabel={node => node.id}
        onNodeClick={handleNodeClick}
        width={600}
        height={400}
      />
    </div>
  );
};

export default ClassGraph;