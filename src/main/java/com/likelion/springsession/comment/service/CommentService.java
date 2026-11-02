package com.likelion.springsession.comment.service;

import com.likelion.springsession.comment.dto.CommentCreateRequest;
import com.likelion.springsession.comment.dto.CommentResponse;
import com.likelion.springsession.comment.dto.CommentUpdateRequest;
import com.likelion.springsession.comment.entity.Comment;
import com.likelion.springsession.comment.exception.CommentAccessDeniedException;
import com.likelion.springsession.comment.repository.CommentRepository;
import com.likelion.springsession.post.entity.Post;
import com.likelion.springsession.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;

import com.likelion.springsession.user.entity.User;
import com.likelion.springsession.user.exception.UserNotFoundException;
import com.likelion.springsession.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse addComment(Long postId, Long userId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Comment saved = commentRepository.save(new Comment(post, author, request.getContent()));
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
    public CommentResponse updateComment(Long postId, Long commentId, Long userId, CommentUpdateRequest request) {
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

        // 작성자 확인
        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        comment.updateContent(request.getContent());
        return new CommentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException();
        }

        if (!comment.isAuthor(userId)) {
            throw new CommentAccessDeniedException();
        }

        post.removeComment(comment);
    }
}
