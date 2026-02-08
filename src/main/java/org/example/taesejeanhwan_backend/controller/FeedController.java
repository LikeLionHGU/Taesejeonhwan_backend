package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.feed.response.*;
import org.example.taesejeanhwan_backend.dto.feed.request.*;
import org.example.taesejeanhwan_backend.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    public final ReviewService reviewService;
    public final WishService wishService;
    public final ContentService contentService;
    public final UserContentService userContentService;

    @PostMapping("/reviews")
    public FeedResponseResult addReview(@RequestBody FeedRequestAddReview feedAddReview) {
        return reviewService.addReview(feedAddReview);
    }

    @PostMapping("/wish")
    public FeedResponseResult addWish(@RequestBody FeedResponseAddWish feedResponseAddWish) {
        return wishService.addWish(feedResponseAddWish);
    }

    @GetMapping("/{user_id}/wish")
    public List<FeedResponseGetWish> getWishList(@PathVariable Long user_id) {
        return wishService.getWishList(user_id);
    }

    @GetMapping("/{user_id}/{content_id}/review")
    public FeedResponseGetContent getContentReview(@PathVariable Long user_id, Long content_id) {
        return reviewService.getContentReview(user_id, content_id);
    }

    @GetMapping("/{content_id}/reviews")
    public List<FeedResponseGetReview> getReviews(@PathVariable Long content_id) {
        return reviewService.getAllReviews(content_id);
    }

    @GetMapping("/{user_id}/contents")
    public List<FeedResponseGetUserContent> getUserContents(@PathVariable Long user_id) {
        return userContentService.getUserContents(user_id);
    }

    @GetMapping("/{mode}")
    public FeedResponseUserAndContent getUserAndContent(@PathVariable String mode) {
        return userContentService.getUserAndContent(mode);
    }

    @GetMapping("/{user_id}/search/{keyword}")
    public FeedResponseSearch search(@PathVariable Long user_id, String keyword) {
        return contentService.search(user_id, keyword);
    }

    @PutMapping("/reviews")
    public FeedResponseReviewUpdate updateReview(@RequestBody FeedRequestReviewUpdate feedRequestReviewUpdate) {
        return reviewService.updateReview(feedRequestReviewUpdate);
    }

    @DeleteMapping("/reviews")
    public FeedResponseResult deleteReview(@RequestBody FeedRequestReviewDelete feedRequestReviewDelete) {
        return reviewService.deleteReview(feedRequestReviewDelete);
    }


}
