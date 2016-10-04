package de.sagr.ci.bamboo.impl;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.chains.ChainStage;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinition;
import com.atlassian.bamboo.plan.artifact.ArtifactDefinitionManager;
import com.atlassian.bamboo.spring.ComponentAccessor;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by grebe on 29.08.2016.
 */
public class ArtifactExpiryTaskConfigurator extends AbstractTaskConfigurator {

    public static final String ARTIFACT_EXPIRY_PARAM_NAME = "artifactsToExpire";

    private static final String ARTIFACT_DEFINITION_LIST = "artifactDefinitionList";

    @Override
    public Map<String, String> generateTaskConfigMap(ActionParametersMap params, TaskDefinition previousTaskDefinition) {
        final Map<String, String> configs = super.generateTaskConfigMap(params, previousTaskDefinition);
        configs.put(ARTIFACT_EXPIRY_PARAM_NAME, getArtifactsToExpire(params));

        return configs;
    }

    private String getArtifactsToExpire(ActionParametersMap params) {
        final String[] artifactsNames = params.getStringArray(ARTIFACT_EXPIRY_PARAM_NAME);
        final StringBuilder builder = new StringBuilder();
        if (artifactsNames != null) {
            final Iterator<String> iter = Arrays.asList(artifactsNames).iterator();
            while (iter.hasNext()) {
                builder.append(iter.next());
                if (iter.hasNext()) {
                    builder.append(";");
                }
            }
        }

        return builder.toString();
    }

    @Override
    public void populateContextForCreate(Map<String, Object> context) {
        super.populateContextForCreate(context);

        context.put(ARTIFACT_DEFINITION_LIST, getAllArtifactDefinitions(context));
    }

    @Override
    public void populateContextForEdit(Map<String, Object> context, TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);

        final String artifactsToExpire = taskDefinition.getConfiguration().get(ARTIFACT_EXPIRY_PARAM_NAME);
        context.put(ARTIFACT_EXPIRY_PARAM_NAME, StringUtils.split(artifactsToExpire, ";"));
        context.put(ARTIFACT_DEFINITION_LIST, getAllArtifactDefinitions(context));
    }

    private List<ArtifactDefinition> getAllArtifactDefinitions(Map<String, Object> context) {
        final Job plan = (Job) context.get("plan");
        final List<ArtifactDefinition> artifactDefinitions = new ArrayList<>();
        for (ChainStage nextChain : plan.getParent().getAllStages()) {
            for (Job nextJob : nextChain.getAllJobs()) {
                artifactDefinitions.addAll(nextJob.getArtifactDefinitions());
            }
        }

        return artifactDefinitions;
    }
}
