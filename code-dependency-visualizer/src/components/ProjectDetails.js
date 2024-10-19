import React from 'react';
import { getCurrentProjectData } from '../api/api';

const ProjectDetails = () => {
    const project = getCurrentProjectData();

    if (!project) {
        return <div>No project data available</div>;
    }

    return (
        <div>
            <h1>{project.name || 'Unnamed Project'}</h1>
            <p>Status: {project.status || 'Unknown'}</p>
            <p>Path: {project.path || 'Not specified'}</p>
            <h2>Modules</h2>
            {project.modules && project.modules.length > 0 ? (
                project.modules.map((module, index) => (
                    <div key={index}>
                        <h3>{module.name}</h3>
                        <p>Path: {module.path}</p>
                        <h4>Packages</h4>
                        {module.packages && module.packages.length > 0 ? (
                            module.packages.map((pkg, pkgIndex) => (
                                <div key={pkgIndex}>
                                    <h5>{pkg.name}</h5>
                                    <ul>
                                        {pkg.classes && pkg.classes.map((cls, clsIndex) => (
                                            <li key={clsIndex}>
                                                {cls.name}
                                                {cls.innerClasses && cls.innerClasses.length > 0 && (
                                                    <span> (Inner classes: {cls.innerClasses.join(', ')})</span>
                                                )}
                                                <p>{cls.aiDescription}</p>
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            ))
                        ) : (
                            <p>No packages found</p>
                        )}
                    </div>
                ))
            ) : (
                <p>No modules found</p>
            )}
            <h2>Dependencies</h2>
            {project.dependencies && project.dependencies.length > 0 ? (
                <ul>
                    {project.dependencies.map((dep, index) => (
                        <li key={index}>
                            {dep.source} {'>'} {dep.target} ({dep.type})
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No dependencies found</p>
            )}
        </div>
    );
};

export default ProjectDetails;