import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';

const MiniMap = ({ graphData, currentNode }) => {
    const svgRef = useRef();

    useEffect(() => {
        if (!graphData || !graphData.nodes.length) return;

        const svg = d3.select(svgRef.current);
        svg.selectAll("*").remove();

        const width = 150;
        const height = 150;

        const simulation = d3.forceSimulation(graphData.nodes)
            .force("link", d3.forceLink(graphData.links).id(d => d.id))
            .force("charge", d3.forceManyBody())
            .force("center", d3.forceCenter(width / 2, height / 2));

        const link = svg.append("g")
            .selectAll("line")
            .data(graphData.links)
            .enter().append("line")
            .attr("stroke", "#999")
            .attr("stroke-opacity", 0.6);

        const node = svg.append("g")
            .selectAll("circle")
            .data(graphData.nodes)
            .enter().append("circle")
            .attr("r", 3)
            .attr("fill", d => d === currentNode ? "red" : "#69b3a2");

        simulation.on("tick", () => {
            link
                .attr("x1", d => d.source.x)
                .attr("y1", d => d.source.y)
                .attr("x2", d => d.target.x)
                .attr("y2", d => d.target.y);

            node
                .attr("cx", d => d.x)
                .attr("cy", d => d.y);
        });

    }, [graphData, currentNode]);

    return (
        <svg ref={svgRef} style={{ position: 'absolute', bottom: 10, right: 10, background: 'rgba(255,255,255,0.7)' }} width={150} height={150}></svg>
    );
};

export default MiniMap;