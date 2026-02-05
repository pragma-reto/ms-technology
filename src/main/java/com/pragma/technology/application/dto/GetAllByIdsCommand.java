package com.pragma.technology.application.dto;

import java.util.List;

public record GetAllByIdsCommand(
        List<String> ids
) {

}
