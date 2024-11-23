package ucan.edu.academic.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResponseDTO {

    private String status;
    private String message;
    private List<?> data;

    // Getters and Setters

}
