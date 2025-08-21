package org.sopt.controller.follow.dto;

import java.util.List;

public record FollowerListDtoRes(
        List<FollowerDtoRes> followerList
) {}