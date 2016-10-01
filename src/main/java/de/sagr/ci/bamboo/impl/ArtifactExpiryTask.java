package de.sagr.ci.bamboo.impl;

import com.atlassian.bamboo.artifact.Artifact;
import com.atlassian.bamboo.build.CustomBuildProcessor;
import com.atlassian.bamboo.build.CustomBuildProcessorServer;
import com.atlassian.bamboo.build.artifact.ArtifactHandler;
import com.atlassian.bamboo.build.artifact.ArtifactHandlingUtils;
import com.atlassian.bamboo.build.artifact.ArtifactManager;
import com.atlassian.bamboo.buildqueue.manager.AgentManager;
import com.atlassian.bamboo.spring.ComponentAccessor;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.task.AbstractBuildTask;
import com.atlassian.plugin.PluginAccessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by grebe on 27.08.2016.
 */
public class ArtifactExpiryTask implements TaskType {

    @NotNull
    @Override
    public TaskResult execute(@NotNull TaskContext taskContext) throws TaskException {
        Artifact artifact = taskContext.getBuildContext().getArtifactContext().getSharedArtifactsFromPreviousStages().values().iterator().next();
        ArtifactHandler artifactHandler = ArtifactHandlingUtils.getArtifactHandlerForLink(ComponentAccessor.PLUGIN_ACCESSOR.get(), artifact.getLinkType());
        artifactHandler.removeArtifactFromStorage(artifact, null);

        return TaskResultBuilder
                .newBuilder(taskContext)
                .build();
    }
}
