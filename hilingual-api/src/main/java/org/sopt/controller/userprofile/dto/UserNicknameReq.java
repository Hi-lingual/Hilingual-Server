package org.sopt.controller.userprofile.dto;

import lombok.NonNull;

public record UserNicknameReq(
        @NonNull String nickname
){}