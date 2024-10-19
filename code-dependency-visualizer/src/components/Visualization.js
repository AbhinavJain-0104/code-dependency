import React, { useEffect, useRef, useState } from 'react';
import ForceGraph3D from '3d-force-graph';
import SpriteText from 'three-spritetext';
import * as d3 from 'd3';

const Visualization = ({ data }) => {
    const mountRef = useRef(null);
    const graphRef = useRef(null);
    const [expandedNodes, setExpandedNodes] = useState(new Set());

    useEffect(() => {
        if (!data || !data.modules || !mountRef.current) return;

        const Graph = ForceGraph3D()(mountRef.current)
            .width(mountRef.current.clientWidth)
            .height(mountRef.current.clientHeight)
            .backgroundColor('#1a1a2e')
            .nodeLabel(node => `${node.name}`)
            .nodeAutoColorBy('group')
            .nodeThreeObject(node => {
                const sprite = new SpriteText(node.name);
                sprite.color = node.color;
                sprite.textHeight = 5;
                return sprite;
            })
            .onNodeClick(node => {
                if (node.group === 'module' || node.group === 'package') {
                    toggleNodeExpansion(node.id);
                }
            })
            .d3Force('charge', d3.forceManyBody().strength(-120));

        const { nodes, links } = convertDataToGraphFormat(data);
        Graph.graphData({ nodes, links });

        graphRef.current = Graph;
    }, [data, expandedNodes]);

    const toggleNodeExpansion = (nodeId) => {
        setExpandedNodes(prev => {
            const newSet = new Set(prev);
            if (newSet.has(nodeId)) {
                newSet.delete(nodeId);
            } else {
                newSet.add(nodeId);
            }
            return newSet;
        });
    };
    const convertDataToGraphFormat = (data) => {
        const nodes = [];
        const links = [];
    
        nodes.push({ id: data.name, name: data.name, group: 'project', color: '#FFA500' });
    
        data.modules.forEach(module => {
            nodes.push({ id: module.name, name: module.name, group: 'module', color: '#4CAF50' });
            links.push({ source: data.name, target: module.name });
    
            if (expandedNodes.has(module.name)) {
                module.packages.forEach(pkg => {
                    const pkgId = `${module.name}.${pkg.name}`;
                    nodes.push({ id: pkgId, name: pkg.name, group: 'package', color: '#2196F3' });
                    links.push({ source: module.name, target: pkgId });
    
                    if (expandedNodes.has(pkgId)) {
                        pkg.classes.forEach(cls => {
                            const clsId = `${pkgId}.${cls.name}`;
                            nodes.push({ id: clsId, name: cls.name, group: 'class', color: '#9C27B0' });
                            links.push({ source: pkgId, target: clsId });
                        });
                    }
                });
            }
        });
    
        return { nodes, links };
    };
    return <div ref={mountRef} style={{ width: '100%', height: '100%' }} />;
};

export default Visualization;