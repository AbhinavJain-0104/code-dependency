

// import React, { useCallback, useRef, useEffect, useState } from 'react';
// import ForceGraph2D from 'react-force-graph-2d';
// import './Visualization.css';

// const Visualization = ({ projectData, onClassSelect }) => {
//   const forceGraphRef = useRef();
//   const [dimensions, setDimensions] = useState({ width: 800, height: 600 });
//   const [highlightNodes, setHighlightNodes] = useState(new Set());
//   const [highlightLinks, setHighlightLinks] = useState(new Set());
//   const [hoverNode, setHoverNode] = useState(null);

//   useEffect(() => {
//     const updateDimensions = () => {
//       const container = document.querySelector('.visualization-container');
//       if (container) {
//         setDimensions({
//           width: container.clientWidth,
//           height: container.clientHeight
//         });
//       }
//     };

//     window.addEventListener('resize', updateDimensions);
//     updateDimensions();

//     return () => window.removeEventListener('resize', updateDimensions);
//   }, []);

//   const graphData = React.useMemo(() => {
//     if (!projectData || !projectData.modules || projectData.modules.length === 0) {
//       return { nodes: [], links: [] };
//     }

//     const nodes = [];
//     const links = [];
//     const nodeMap = new Map();

//     projectData.modules.forEach(module => {
//       const moduleNode = {
//         id: module.name,
//         group: 'module',
//         moduleData: module,
//       };
//       nodes.push(moduleNode);
//       nodeMap.set(module.name, moduleNode);

//       if (module.packages) {
//         module.packages.forEach(pkg => {
//           const packageNode = {
//             id: `${module.name}.${pkg.name}`,
//             group: 'package',
//             packageData: pkg,
//           };
//           nodes.push(packageNode);
//           nodeMap.set(packageNode.id, packageNode);
//           links.push({ source: module.name, target: packageNode.id });

//           if (pkg.classes) {
//             pkg.classes.forEach(cls => {
//               const classNode = {
//                 id: `${packageNode.id}.${cls.name}`,
//                 group: 'class',
//                 classData: cls,
//               };
//               nodes.push(classNode);
//               nodeMap.set(classNode.id, classNode);
//               links.push({ source: packageNode.id, target: classNode.id });

//               if (cls.usedClasses) {
//                 cls.usedClasses.forEach(usedClass => {
//                   const targetNode = nodes.find(n => n.id.endsWith(`.${usedClass}`));
//                   if (targetNode) {
//                     links.push({ source: classNode.id, target: targetNode.id });
//                   }
//                 });
//               }
//             });
//           }
//         });
//       }
//     });

//     return { nodes, links };
//   }, [projectData]);

//   const handleNodeHover = node => {
//     highlightNodes.clear();
//     highlightLinks.clear();
//     if (node) {
//       highlightNodes.add(node);
//       graphData.links.forEach(link => {
//         if (link.source === node || link.target === node) {
//           highlightLinks.add(link);
//           highlightNodes.add(link.source);
//           highlightNodes.add(link.target);
//         }
//       });
//     }

//     setHoverNode(node || null);
//     setHighlightNodes(new Set(highlightNodes));
//     setHighlightLinks(new Set(highlightLinks));
//   };

//   const handleNodeClick = useCallback(node => {
//     if (node.group === 'class') {
//       onClassSelect(node.classData);
//     }
//   }, [onClassSelect]);

//   const getNodeColor = useCallback(node => {
//     if (highlightNodes.has(node)) {
//       return node.group === 'module' ? '#ff6b6b' : 
//              node.group === 'package' ? '#4ecdc4' : '#45b7d1';
//     }
//     return node.group === 'module' ? '#ff9ff3' : 
//            node.group === 'package' ? '#54a0ff' : '#5f27cd';
//   }, [highlightNodes]);

//   const getLinkColor = useCallback(() => 'rgba(255, 255, 255, 0.2)', []); // Semi-transparent white for links

//   const getTextColor = useCallback(node => {
//     return node.group === 'module' ? '#ffffff' : 
//            node.group === 'package' ? '#ffffff' : '#ffffff';
//   }, []);

//   if (graphData.nodes.length === 0) {
//     return <div className="visualization">No data to visualize</div>;
//   }

//   return (
//     <div className="visualization">
//       <ForceGraph2D
//         ref={forceGraphRef}
//         graphData={graphData}
//         nodeLabel={node => node.id}
//         nodeColor={getNodeColor}
//         nodeRelSize={8}
//         linkWidth={link => highlightLinks.has(link) ? 2 : 1}
//         linkColor={getLinkColor}
//         linkDirectionalArrowLength={3}
//         linkDirectionalArrowRelPos={1}
//         linkCurvature={0.25}
//         onNodeHover={handleNodeHover}
//         onNodeClick={handleNodeClick}
//         width={dimensions.width}
//         height={dimensions.height}
//         d3AlphaDecay={0.01}
//         d3VelocityDecay={0.08}
//         cooldownTicks={100}
//         onEngineStop={() => {
//           forceGraphRef.current.zoomToFit(400, 60);
//         }}
//         nodeCanvasObject={(node, ctx, globalScale) => {
//           const label = node.id.split('.').pop();
//           const fontSize = 14 / globalScale;
//           ctx.font = `${fontSize}px Arial`;
//           const textWidth = ctx.measureText(label).width;
//           const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2);

//           ctx.fillStyle = getNodeColor(node);
//           ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);

//           ctx.textAlign = 'center';
//           ctx.textBaseline = 'middle';
//           ctx.fillStyle = getTextColor(node);
//           ctx.fillText(label, node.x, node.y);
//         }}
//       />
//       <div className="legend">
//         <div><span style={{backgroundColor: '#ff9ff3', color: '#ffffff'}}>Module</span></div>
//         <div><span style={{backgroundColor: '#54a0ff', color: '#ffffff'}}>Package</span></div>
//         <div><span style={{backgroundColor: '#5f27cd', color: '#ffffff'}}>Class</span></div>
//       </div>
//     </div>
//   );
// };

// export default Visualization;


import React, { useCallback, useRef, useEffect, useState, useMemo } from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import './Visualization.css';

const Visualization = ({ projectData, onClassSelect }) => {
  const forceGraphRef = useRef();
  const [dimensions, setDimensions] = useState({ width: 800, height: 600 });
  const [highlightNodes, setHighlightNodes] = useState(new Set());
  const [highlightLinks, setHighlightLinks] = useState(new Set());
  const [hoverNode, setHoverNode] = useState(null);

  useEffect(() => {
    const updateDimensions = () => {
      const container = document.querySelector('.visualization-container');
      if (container) {
        setDimensions({
          width: container.clientWidth,
          height: container.clientHeight
        });
      }
    };

    window.addEventListener('resize', updateDimensions);
    updateDimensions();

    return () => window.removeEventListener('resize', updateDimensions);
  }, []);

  const graphData = useMemo(() => {
    if (!projectData || !projectData.modules || projectData.modules.length === 0) {
      return { nodes: [], links: [] };
    }

    const nodes = [];
    const links = [];
    const nodeMap = new Map();

    projectData.modules.forEach(module => {
      const moduleNode = {
        id: module.name,
        group: 'module',
        moduleData: module,
      };
      nodes.push(moduleNode);
      nodeMap.set(module.name, moduleNode);

      if (module.packages) {
        module.packages.forEach(pkg => {
          const packageNode = {
            id: `${module.name}.${pkg.name}`,
            group: 'package',
            packageData: pkg,
          };
          nodes.push(packageNode);
          nodeMap.set(packageNode.id, packageNode);
          links.push({ source: module.name, target: packageNode.id });

          if (pkg.classes) {
            pkg.classes.forEach(cls => {
              const classNode = {
                id: `${packageNode.id}.${cls.name}`,
                group: 'class',
                classData: cls,
              };
              nodes.push(classNode);
              nodeMap.set(classNode.id, classNode);
              links.push({ source: packageNode.id, target: classNode.id });

              if (cls.usedClasses) {
                cls.usedClasses.forEach(usedClass => {
                  const targetNode = nodes.find(n => n.id.endsWith(`.${usedClass}`));
                  if (targetNode) {
                    links.push({ source: classNode.id, target: targetNode.id });
                  }
                });
              }
            });
          }
        });
      }
    });

    return { nodes, links };
  }, [projectData]);

  const handleNodeHover = useCallback((node) => {
    if (!graphData) return;
    
    highlightNodes.clear();
    highlightLinks.clear();
    if (node) {
      highlightNodes.add(node);
      graphData.links.forEach(link => {
        if (link.source === node || link.target === node) {
          highlightLinks.add(link);
          highlightNodes.add(link.source);
          highlightNodes.add(link.target);
        }
      });
    }

    setHoverNode(node || null);
    setHighlightNodes(new Set(highlightNodes));
    setHighlightLinks(new Set(highlightLinks));
  }, [graphData]);

  const handleNodeClick = useCallback(node => {
    if (node.group === 'class') {
      onClassSelect(node.classData);
    }
  }, [onClassSelect]);

  const getNodeColor = useCallback(node => {
    if (highlightNodes.has(node)) {
      return node.group === 'module' ? '#ff6b6b' : 
             node.group === 'package' ? '#4ecdc4' : '#45b7d1';
    }
    return node.group === 'module' ? '#ff9ff3' : 
           node.group === 'package' ? '#54a0ff' : '#5f27cd';
  }, [highlightNodes]);

  const getLinkColor = useCallback(link => 
    highlightLinks.has(link) ? '#ffa500' : '#808080', [highlightLinks]
  );

  const getTextColor = useCallback(() => '#ffffff', []);

  if (!graphData || graphData.nodes.length === 0) {
    return <div className="visualization">No data to visualize</div>;
  }

  return (
    <div className="visualization">
      <ForceGraph2D
        ref={forceGraphRef}
        graphData={graphData}
        nodeLabel={node => node.id}
        nodeColor={getNodeColor}
        nodeRelSize={8}
        linkWidth={link => highlightLinks.has(link) ? 2 : 1}
        linkColor={getLinkColor}
        linkDirectionalArrowLength={5}
        linkDirectionalArrowRelPos={1}
        linkCurvature={0.25}
        onNodeHover={handleNodeHover}
        onNodeClick={handleNodeClick}
        width={dimensions.width}
        height={dimensions.height}
        d3AlphaDecay={0.01}
        d3VelocityDecay={0.08}
        cooldownTicks={100}
        onEngineStop={() => {
          forceGraphRef.current.zoomToFit(400, 60);
        }}
        nodeCanvasObject={(node, ctx, globalScale) => {
          const label = node.id.split('.').pop();
          const fontSize = 14 / globalScale;
          ctx.font = `${fontSize}px Arial`;
          const textWidth = ctx.measureText(label).width;
          const bckgDimensions = [textWidth, fontSize].map(n => n + fontSize * 0.2);

          ctx.fillStyle = getNodeColor(node);
          ctx.fillRect(node.x - bckgDimensions[0] / 2, node.y - bckgDimensions[1] / 2, ...bckgDimensions);

          ctx.textAlign = 'center';
          ctx.textBaseline = 'middle';
          ctx.fillStyle = getTextColor(node);
          ctx.fillText(label, node.x, node.y);
        }}
      />
      <div className="legend">
        <div><span style={{backgroundColor: '#ff9ff3', color: '#ffffff'}}>Module</span></div>
        <div><span style={{backgroundColor: '#54a0ff', color: '#ffffff'}}>Package</span></div>
        <div><span style={{backgroundColor: '#5f27cd', color: '#ffffff'}}>Class</span></div>
      </div>
    </div>
  );
};

export default Visualization;