package com.cms.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiComplaintResponse {

    private String category;

    private String priority;
}