package org.sopt.controller.widget.api;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.widget.dto.res.WidgetTopicResponse;
import org.sopt.controller.widget.service.WidgetService;
import org.sopt.jwt.annotation.UserIdOrNull;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/widget")
@RequiredArgsConstructor
public class WidgetController{
    private final WidgetService widgetService;

    @GetMapping("/topic")
    public ResponseEntity<WidgetTopicResponse> getTopicWidget(
            @UserIdOrNull final Long userId,
            @UserTimezone final UserZone userZone
            ){
        return ResponseEntity.ok(widgetService.getTopicWidget(userId,userZone.zoneId()));
    }
}