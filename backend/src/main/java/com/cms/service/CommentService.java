package com.cms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.dto.CommentRequest;
import com.cms.model.Comment;
import com.cms.model.Complaint;
import com.cms.model.User;
import com.cms.repository.CommentRepository;
import com.cms.repository.ComplaintRepository;
import com.cms.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment addComment(

            String email,

            CommentRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        Complaint complaint =
                complaintRepository
                        .findById(
                                request.getComplaintId()
                        )
                        .orElseThrow();

        Comment comment =
                Comment.builder()

                        .message(
                                request.getMessage()
                        )

                        .createdAt(
                                LocalDateTime.now()
                        )

                        .complaint(
                                complaint
                        )

                        .user(
                                user
                        )

                        .build();

        return commentRepository.save(
                comment
        );
    }

    public List<Comment> getComments(
            Long complaintId
    ) {

        return commentRepository
                .findByComplaintIdOrderByCreatedAtAsc(
                        complaintId
                );
    }
}