package org.sopt.controller.follow.dto;

import java.util.List;

public record FollowingListDtoRes(
        List<FollowingDtoRes> followingList
) { }