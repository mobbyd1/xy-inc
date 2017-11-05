package com.zup.api.dto;

import com.zup.util.AttributeType;

import java.util.Map;

/**
 * Created by ruhandosreis on 03/11/17.
 */
public class CreateModelDTO {

    private String modelName;
    private Map<String, AttributeType> attributes;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, AttributeType> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, AttributeType> attributes) {
        this.attributes = attributes;
    }
}
