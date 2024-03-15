package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validationGroup.AdvanceInfo;
import ru.practicum.shareit.validationGroup.BasicInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {

    @NotNull(groups = {BasicInfo.class})
    @Size(max = 255, groups = {AdvanceInfo.class})
    private String name;

    @NotNull(groups = {BasicInfo.class})
    @Size(max = 512, groups = {AdvanceInfo.class})
    @Email(groups = {AdvanceInfo.class})
    private String email;


}
