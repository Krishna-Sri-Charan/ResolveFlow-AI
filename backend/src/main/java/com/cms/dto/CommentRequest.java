package com.cms.dto;

import lombok.Data;

@Data
public class CommentRequest {

    private Long complaintId;

    private String message;
}