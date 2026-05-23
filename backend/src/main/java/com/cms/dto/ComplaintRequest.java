package com.cms.dto;

import lombok.Data;

@Data
public class ComplaintRequest {

    private String title;

    private String description;
    
    private String aiCategory;

    private String priority;
}