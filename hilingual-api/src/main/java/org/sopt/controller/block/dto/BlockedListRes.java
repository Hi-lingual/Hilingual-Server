package org.sopt.controller.block.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;

import java.util.List;

public record BlockedListRes(
        List<UserProfileSummaryRes> blockList
) {

}
