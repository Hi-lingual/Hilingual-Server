package org.sopt.controller.recoveryticket.dto;

public record RecoveryTicketUsedRes(
    Long ticketId,
    String targetDate,
    Long remainingChances
) {}