package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationGroup.AdvanceInfo;
import ru.practicum.shareit.validationGroup.BasicInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreationDto {
    @NotNull(groups = BasicInfo.class)
    @NotBlank(groups = BasicInfo.class)
    @Size(min = 3, max = 200, groups = AdvanceInfo.class)
    String name;
    @NotNull(groups = BasicInfo.class)
    @NotBlank(groups = BasicInfo.class)
    @Size(max = 500, groups = AdvanceInfo.class)
    String description;
    @NotNull(groups = BasicInfo.class)
    Boolean available;

    Long requestId;
}
