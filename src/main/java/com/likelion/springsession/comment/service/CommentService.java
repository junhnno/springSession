package com.likelion.springsession.comment.service;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.comment.repository.CommentRepository;
import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse addComment(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        Comment saved = commentRepository.save(new Comment(post, request.getContent()));
        return new CommentResponse(saved);
    }

    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByPost(post)) {
            responses.add(new CommentResponse(comment));
        }
        return responses;
    }

    @Transactional
    public CommentResponse updateComment(Long postId,Long commentId, CommentUpdateRequest request) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow();
        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        // 게시글-댓글 매핑 확인
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException();
        }

        comment.updateContent(request.getContent());
        return new CommentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException();
        }

        post.removeComment(comment);
    }
}
