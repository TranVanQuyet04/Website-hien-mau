package com.quyet.superapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class BloodTypeAndComponentResponse {
    private List<BloodTypeDTO> bloodTypes;
    private List<BloodComponentDTO> bloodComponents;
}
