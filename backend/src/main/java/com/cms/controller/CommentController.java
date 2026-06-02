package com.cms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.dto.ApiResponse;
import com.cms.dto.CommentRequest;
import com.cms.model.Comment;
import com.cms.service.CommentService;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ApiResponse<Comment> addComment(

            Principal principal,

            @RequestBody
            CommentRequest request
    ) {

        return ApiResponse
                .<Comment>builder()

                .success(true)

                .message(
                        "Comment added"
                )

                .data(
                        commentService.addComment(

                                principal.getName(),

                                request
                        )
                )

                .build();
    }

    @GetMapping("/{complaintId}")
    public ApiResponse<List<Comment>> getComments(
            @PathVariable
            Long complaintId
    ) {

        return ApiResponse
                .<List<Comment>>builder()

                .success(true)

                .message(
                        "Comments fetched"
                )

                .data(
                        commentService
                                .getComments(
                                        complaintId
                                )
                )

                .build();
    }
}